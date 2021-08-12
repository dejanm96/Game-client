package client.controller;

import java.util.List;

import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import client.communication.Communication;
import client.helpers.DataConverter;
import client.map.models.Node;
import client.model.GameStateModel;

/**
 * GameStateController activates the thread which is updating the GameStateModel
 * regulary keeping the data locally up to date with the server.
 * @author Dejan Micic
 */
public class GameStateController implements Runnable {
	
	private Communication serverCommunication; 
	private GameStateModel gameState;
	
	private DataConverter dataConverter = new DataConverter();
	
	protected static final long SLEEP_TIME = 400;
	
	public GameStateController(GameStateModel model, Communication communication) {
		this.serverCommunication = communication;
		this.gameState = model;
	}

	@Override
	public void run() {

		while (!gameState.gameEnd()) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GameState serverGamestate = serverCommunication.gameState();
			if (!serverGamestate.getGameStateId().equals(gameState.getGameStateID())) {
				gameState.setGameStateID(serverGamestate.getGameStateId());
				for (PlayerState player : serverGamestate.getPlayers()) {
					if (player.getUniquePlayerID().equals(serverCommunication.getUniquePlayerId())) {
						gameState.setMyPlayerState(dataConverter.getPlayerGameState(player.getState()));
						if(player.hasCollectedTreasure())
							gameState.setTreasureCollected(true);
					}
				}

				if (serverGamestate.getMap().isPresent()) {
					List<Node> list = dataConverter.convertServerToClientMap((serverGamestate.getMap().get().getMapNodes()));
					gameState.setFullMap(list);
				}
			}

		}
	}
	
	

}
