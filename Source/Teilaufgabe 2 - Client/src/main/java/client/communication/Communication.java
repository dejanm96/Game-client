package client.communication;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.EMove;
import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import client.exceptions.OutOfMoveException;
import client.helpers.DataConverter;
import client.map.enums.Direction;
import client.map.models.FullMap;
import reactor.core.publisher.Mono;

/**
 * Class for communication with the server
 * @author Dejan Micic
 *
 */
public class Communication {
	
	private Logger logger = LoggerFactory.getLogger(Communication.class);
	
	private DataConverter dataConverter = new DataConverter();
	
	int counter = 1;
	
	private String serverBaseUrl;
	private String gameId;
	private String uniquePlayerId;
	
	
	public Communication(String serverBaseUrl, String gameId) {
		this.serverBaseUrl = serverBaseUrl;
		this.gameId = gameId;
	}

	
	public String getUniquePlayerId() {
		return uniquePlayerId;
	}
	
	/**
	 * Player registration class will register the clients to the game and 
	 * send the unique player ID as result of the registration.
	 */
	public void registerPlayer() {
		WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) //the network protocol uses XML
			    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
			    .build();
			
		PlayerRegistration playerReg = new PlayerRegistration("Dejan", "Micic", "01549172");
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + gameId + "/players")
				.body(BodyInserters.fromValue(playerReg)) // specify the data which is set to the server
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		@SuppressWarnings("unchecked")
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
	
		if(resultReg.getState() == ERequestState.Error) {
			logger.error("Client registration error: " + resultReg.getExceptionMessage());
		}
		else {
			UniquePlayerIdentifier uniqueID = resultReg.getData().get();
			logger.info("Player successfuly registered with the id:"+uniqueID.getUniquePlayerID());
			uniquePlayerId = uniqueID.getUniquePlayerID(); 
		}
	}
	
	
	
	/**
	 * Class for getting the gameState off of the server.
	 * GameState contains info such as GameStateID, my player state,
	 * FullMap...
	 * @return gameState
	 */
	public GameState gameState() {
		WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) //the network protocol uses XML
			    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
			    .build();
		
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameId + "/states/" + uniquePlayerId)
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server
		
		@SuppressWarnings("unchecked")	
		ResponseEnvelope<GameState> requestResult = webAccess.block();
		
		if(requestResult.getState() == ERequestState.Error) {	
			System.out.println("Client error, errormessage: " + requestResult.getExceptionMessage());
			throw new NullPointerException();
		}
		else { 
			return requestResult.getData().get();
		}
	}
	
		
	public String getUniqueNo() {
		return this.uniquePlayerId;
	}


	/**
	 * This function is used to send the map halfs to the server.
	 * The map is converted into the server-understandable map and then sent.
	 * @param map
	 */
	public void sendMap(FullMap map) {
		
		Collection<HalfMapNode> nodes = dataConverter.convertClientToServerMap(map);
				
		WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) //the network protocol uses XML
			    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
			    .build();
		
		HalfMap hMap = new HalfMap(uniquePlayerId, nodes);
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + gameId + "/halfmaps")
				.body(BodyInserters.fromValue(hMap)) // specify the data which is set to the server
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

				@SuppressWarnings("unchecked")
				ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
	
			if(resultReg.getState() == ERequestState.Error) {
				logger.error("Error while sending half map: " + resultReg.getExceptionMessage());
			}
		else {
			logger.info("Half map sent successfully!");
		}
	}
	
	/**
	 * Is used when sending player movement, will get a string with the way
	 * which is then sent to the server.
	 * @param way
	 */
	public void movePlayer(Direction way) {
		
		WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) //the network protocol uses XML
			    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
			    .build();
		

		EMove moveEMove = dataConverter.convertMove(way);
		PlayerMove playerMove = null;

		
		playerMove = PlayerMove.of(uniquePlayerId, moveEMove);
		
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + gameId + "/moves")
				.body(BodyInserters.fromValue(playerMove)) // specify the data which is set to the server
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

				@SuppressWarnings("unchecked")
				ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
	
			if(resultReg.getState() == ERequestState.Error) {
				logger.error("Error while sending a move : " + resultReg.getExceptionMessage());
			}
		else {
			logger.info("Move sent successfully!");
			counter++;
			System.out.println(counter + " moves");
			if(counter >= 100)
				throw new OutOfMoveException();
		}

	}
	
}
