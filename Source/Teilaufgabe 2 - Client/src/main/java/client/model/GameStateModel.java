package client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import client.map.enums.PlayerGameState;
import client.map.models.FullMap;
import client.map.models.Node;
/**
 * GameStateModel i singleton class which is used to help with the server communication.
 * With this model the gamestate from the server is set locally.
 * @author Dejan Micic
 *
 */
public class GameStateModel {
	
	private static GameStateModel gameStateInstance = null; 
	private String uniquePlayerId;
	private String gameStateID;
	private FullMap fullMap;
	private PlayerGameState myPlayerState = PlayerGameState.ShouldWait; 
	private boolean treasureCollected = false;
	
	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	private GameStateModel() {
		this.fullMap = new FullMap();
	}

	public static GameStateModel getGameState() {
		
		if(gameStateInstance == null) {
			gameStateInstance = new GameStateModel();
		}
		return gameStateInstance;
	}
	
	public void addListener(PropertyChangeListener view) {
		changes.addPropertyChangeListener(view);
	}
	
	public String getUniquePlayerId() {
		return uniquePlayerId;
	}

	public void setUniquePlayerId(String uniquePlayerId) {
		this.uniquePlayerId = uniquePlayerId;
	}

	public String getGameStateID() {
		return gameStateID;
	}

	public void setGameStateID(String gameStateID) {
		this.gameStateID = gameStateID;
	}


	public synchronized FullMap getFullMap() {
		return fullMap;
	}
	
	
	public synchronized void setFullMap(List<Node> list) {

		
 		this.fullMap.setMapNodes(list);
 		
 		if(fullMap.getMapNodes().size() == 64)
 			changes.firePropertyChange("!!!Map has been updated!!!", null, fullMap);
	}

	
	public synchronized PlayerGameState getMyPlayerState() {
		return myPlayerState;
	}
	
	public synchronized void setMyPlayerState(PlayerGameState myPlayerState) {
		PlayerGameState oldState = this.myPlayerState;
		this.myPlayerState = myPlayerState;
		if(myPlayerState.equals(PlayerGameState.Won) || myPlayerState.equals(PlayerGameState.Lost))
			changes.firePropertyChange("State", oldState, myPlayerState);
	}
	
	public boolean gameEnd() {
		return myPlayerState.equals(PlayerGameState.Lost) || myPlayerState.equals(PlayerGameState.Won);
	}

	public boolean isTreasureCollected() {
		return treasureCollected;
	}

	public void setTreasureCollected(boolean treasureCollected) {
		this.treasureCollected = treasureCollected;
		if(!gameEnd())
			changes.firePropertyChange("Treasure collected", null, this.treasureCollected);
	}
	public boolean getTreasureCollected() {
		return this.treasureCollected;

	}
}
