package server.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.database.GameDatabase;
import server.enums.PlayerGameState;
import server.gamestate.LocalGameState;
import server.map.Node;

public class Player {

	private String uniquePlayerID;
	private List<Node> playerHalfMap = new ArrayList<>();
	private String playerFirstName;
	private String playerLastName;
	private String playerStudentID;
	private PlayerGameState gameStateValue;
	private LocalGameState gameState;
	private Logger logger = LoggerFactory.getLogger(Player.class);
	private boolean treasureCollected;
	private GameDatabase database = GameDatabase.singleObject();
	private boolean mapAlreadySent;
	private Node castlePosition;
	
	public Player() { 
		treasureCollected = false;
		gameState = new LocalGameState();
		mapAlreadySent = false;
		setCastlePosition(null);
		}

	public String getUniquePlayerID() {
		return uniquePlayerID;
	}

	public void setUniquePlayerID(String uniquePlayerID) {
		this.uniquePlayerID = uniquePlayerID;
	}

	public List<Node> getPlayerHalfMap() {
		return playerHalfMap;
	}

	public void setPlayerHalfMap(List<Node> playerHalfMap, String gameID, String uniquePlayerID) {
		Game game = database.getGameByID(gameID);
		gameState.setStateForPlayers(database.getGameByID(gameID).getPlayers(), database.getPlayerByUniquePlayerID(gameID, uniquePlayerID));
		game.setGameStateID(UUID.randomUUID().toString());
		setMapAlreadySent(true);
		logger.info("Halfmap was set for player with uniquePlayerID: " + uniquePlayerID + " for the game " + gameID);
		this.playerHalfMap = playerHalfMap;
		if(game.checkIfBothMapsAreSent()) {
			game.createWholeMap();
		}
	}

	public String getPlayerFirstName() {
		return playerFirstName;
	}

	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}

	public String getPlayerLastName() {
		return playerLastName;
	}

	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}

	public String getPlayerStudentID() {
		return playerStudentID;
	}

	public void setPlayerStudentID(String playerStudentID) {
		this.playerStudentID = playerStudentID;
	}

	public PlayerGameState getGameStateValue() {
		return gameStateValue;
	}

	public void setGameStateValue(PlayerGameState gameStateValue) {
		this.gameStateValue = gameStateValue;
	}

	public boolean getTreasureCollected() {
		return treasureCollected ;
	}

	public boolean isMapAlreadySent() {
		return mapAlreadySent;
	}

	public void setMapAlreadySent(boolean mapAlreadySent) {
		this.mapAlreadySent = mapAlreadySent;
	}

	public Node getCastlePosition() {
		return castlePosition;
	}

	public void setCastlePosition(Node castlePosition) {
		this.castlePosition = castlePosition;
	}
	
}
