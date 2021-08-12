package client.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.communication.Communication;
import client.map.enums.Direction;
import client.map.enums.KindOfSearch;
import client.map.enums.PlayerGameState;
import client.map.generator.MapGenerator;
import client.map.models.FullMap;
import client.map.models.Node;
import client.model.GameStateModel;
import client.moves.CalculateNextMove;
import client.view.View;

/**
 * GameController controls the procedure of the game.
 * 
 * @author Dejan Micic
 *
 */
public class GameController {

	private Communication serverCommunication;
	private GameStateModel gameState;

	private Logger logger = LoggerFactory.getLogger(GameController.class);

	public GameController(Communication communiction) {
		this.serverCommunication = communiction;
		this.gameState = GameStateModel.getGameState();
		@SuppressWarnings("unused")
		View view = new View(gameState);
	}

	/**
	 * Start the game in the main class.
	 */
	public void startGame() {

		serverCommunication.registerPlayer();

		Thread gameChecker = new Thread(new GameStateController(gameState, serverCommunication));
		gameChecker.start();

		logger.info("Started game updating thread - Updating GameState regulary!");

		sendMapHalf();

		int noOfRepetitioin = 0;
		Direction moveWay = null;

		CalculateNextMove calc = new CalculateNextMove();
		while (!gameState.gameEnd()) {

			if (!gameState.getTreasureCollected()) {

				if (gameState.getMyPlayerState().equals(PlayerGameState.ShouldActNext)
						&& gameState.getFullMap().getMapNodes().size() == 64) {
					
					sleep();
					
					calc.setMapToCheck(gameState.getFullMap().getMapNodes());
					calc.setCurrent(calc.getCurrentNode());
					calc.setNeighbors();

					if (noOfRepetitioin == 0) {
						Map<Direction, Integer> way = new HashMap<>();
						way = calc.calculateNextMove(KindOfSearch.TREASURE);
						for (Map.Entry<Direction, Integer> m : way.entrySet()) {
							noOfRepetitioin = m.getValue();
							moveWay = m.getKey();
						}

					} else {
						serverCommunication.movePlayer(moveWay);
						noOfRepetitioin--;
						gameState.setMyPlayerState(PlayerGameState.ShouldWait);
					}

				}

			} else {
				System.out.println("TREASURE HAS BEEN FOUND");
				break;
			}

		}

		while (!gameState.gameEnd()) {

			if (gameState.getMyPlayerState().equals(PlayerGameState.ShouldActNext)
					&& gameState.getFullMap().getMapNodes().size() == 64) {

				sleep();

				calc.setMapToCheck(gameState.getFullMap().getMapNodes());
				calc.setCurrent(calc.getCurrentNode());
				calc.setNeighbors();

				if (noOfRepetitioin == 0) {
					Map<Direction, Integer> way = new HashMap<>();
					way = calc.calculateNextMove(KindOfSearch.FORT);
					for (Map.Entry<Direction, Integer> m : way.entrySet()) {
						noOfRepetitioin = m.getValue();
						moveWay = m.getKey();
					}

				} else {
					serverCommunication.movePlayer(moveWay);
					noOfRepetitioin--;
					gameState.setMyPlayerState(PlayerGameState.ShouldWait);
				}


			} 
		}
	}

	private void sleep() {
		try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Sending the map half
	 */
	private void sendMapHalf() {

		FullMap map = generateMap();

		boolean sent = false;

		while (!sent) {

			if (gameState.getMyPlayerState().equals(PlayerGameState.ShouldActNext)) {
				serverCommunication.sendMap(map);
				sent = true;
				gameState.setMyPlayerState(PlayerGameState.ShouldWait);

			}
		}

	}

	/**
	 * Generating map
	 * 
	 * @return map
	 */
	private FullMap generateMap() {
		MapGenerator generator = new MapGenerator();
		List<Node> newMap = generator.newList();
		FullMap map = new FullMap(newMap);
		return map;
	}

}
