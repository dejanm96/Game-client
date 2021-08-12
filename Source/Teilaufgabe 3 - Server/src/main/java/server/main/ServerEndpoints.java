package server.main;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import server.converter.MapConverter;
import server.database.GameDatabase;
import server.exceptions.ServerException;
import server.game.Player;
import server.gamestate.LocalGameState;
import server.helpers.RandomGameIDCreator;
import server.map.Node;
import server.validation.IRule;

@Controller
@RequestMapping(value = "/games")
public class ServerEndpoints {
	
	private GameDatabase database = GameDatabase.singleObject();
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame() {
		database.deleteGameOlderThenTenMinutes();
		RandomGameIDCreator idCreator = new RandomGameIDCreator();

		if(database.getAllGames().size() >= 999) {
			database.deleteOldest();
		}
		
		String randomID = idCreator.getRandomGameID();
		UniqueGameIdentifier gameIdentifier = new UniqueGameIdentifier(randomID);
		database.addNewGame(randomID);
		return gameIdentifier;
	}


	// example for a POST endpoint based on games/{gameID}/players
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(@PathVariable String gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {

		if (database.getGameByID(gameID) == null)
			throw new ServerException("Registration error", "Not a valid game");

		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage;
		playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		database.registerPlayerToGame(gameID, newPlayerID.getUniquePlayerID(), playerRegistration);

		return playerIDMessage;
	}
	
	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> recieveHalfMap(@PathVariable String gameID,
			@Validated @RequestBody HalfMap halfMap) {
		
		if(database.getGameByID(gameID) == null)
			throw new ServerException("Unique No error", "The given Game ID does not exist!");
		
		if(database.getPlayerByUniquePlayerID(gameID, halfMap.getUniquePlayerID()) == null)
			throw new ServerException("Unique No error", "The given Game ID does not exist!");
		
		if(database.getPlayerByUniquePlayerID(gameID, halfMap.getUniquePlayerID()).isMapAlreadySent()) {
			LocalGameState gameState = new LocalGameState();
			gameState.setLostOrWon(database.getGameByID(gameID).getPlayers(), gameID, halfMap.getUniquePlayerID());
			throw new ServerException("Map error", "The client already sent 1 map!");
		}
		for (IRule eachRule : database.getGameByID(gameID).getRulesList().getAllRules())
			eachRule.sendHalfMap(gameID, halfMap);

		MapConverter mapConverter = new MapConverter();

		List<Node> halfMapList = mapConverter.convertServerToClientMap(halfMap.getNodes());

		Player player = new Player();
		
		player = database.getPlayerByUniquePlayerID(gameID, halfMap.getUniquePlayerID());

		player.setPlayerHalfMap(halfMapList, gameID, halfMap.getUniquePlayerID());

		return new ResponseEnvelope<>();
	}
	
	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> gameState(@PathVariable String gameID,
		@PathVariable String playerID) {
		
		if(database.getGameByID(gameID) == null)
			throw new ServerException("The gameID error", "UniqueGameID not valid!");
			
		if(database.getPlayerByUniquePlayerID(gameID, playerID) == null)
			throw new ServerException("Unique No error", "The given Game ID does not exist!");
		
		
		for(IRule eachRule : database.getGameByID(gameID).getRulesList().getAllRules())
			eachRule.gameState(gameID, playerID);
		
		GameState gamestate = new GameState(Optional.of(database.getGameByID(gameID).getWholeMap(playerID)), database.getGameByID(gameID).getStates(gameID, playerID), database.getGameByID(gameID).getGameStateID());
		//GameState gamestate = new GameState(database.getGameByID(gameID).getStates(gameID, playerID), database.getGameByID(gameID).getGameStateID());
		return new ResponseEnvelope<GameState>(gamestate);
	}


	@ExceptionHandler({ ServerException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(ServerException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
