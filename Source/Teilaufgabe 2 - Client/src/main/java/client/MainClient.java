package client;

import client.communication.Communication;
import client.controller.GameController;



public class MainClient {

	public static void main(String[] args) {
		
		//  http://swe.wst.univie.ac.at:18235/games with your web browser.
		
		String serverBaseUrl = args[1];
		String gameId = args[2];
		
		Communication communication = new Communication(serverBaseUrl, gameId);
		
	
		GameController gameController = new GameController(communication);
		gameController.startGame();

		
	}
}
