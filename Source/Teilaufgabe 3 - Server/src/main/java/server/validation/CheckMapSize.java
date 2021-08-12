package server.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import server.database.GameDatabase;
import server.exceptions.ServerException;
import server.gamestate.LocalGameState;

public class CheckMapSize implements IRule{

	private GameDatabase database = GameDatabase.singleObject();
	private Logger logger = LoggerFactory.getLogger(CheckMapSize.class);
	
	public CheckMapSize() { 	}
	


	@Override
	public void newGame() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void registerPlayer(String gameID, PlayerRegistration playerRegistration) {
		
	}



	@Override
	public void sendHalfMap(String gameID, HalfMap halfMap) {
		checkMapSize(halfMap, gameID, halfMap.getUniquePlayerID());
		
	}



	@Override
	public void gameState(String gameID, String playerID) {
		// TODO Auto-generated method stub
		
	}
	

	
	private void checkMapSize(HalfMap halfMap, String gameId, String uniquePlayerID) {
		LocalGameState localGameState = new LocalGameState();
		if(halfMap.getNodes().size() != 32) {
			localGameState.setLostOrWon(database.getGameByID(gameId).getPlayers() ,gameId, uniquePlayerID);
			logger.error("Map size is not 32!");
			throw new ServerException("Business rules broken", "The map size is not 32!");
		}
	}


}
