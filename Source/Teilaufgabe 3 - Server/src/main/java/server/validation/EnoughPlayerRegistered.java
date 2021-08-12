package server.validation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import server.database.GameDatabase;
import server.exceptions.ServerException;
import server.game.Player;
import server.gamestate.LocalGameState;

public class EnoughPlayerRegistered implements IRule{

	private GameDatabase database = GameDatabase.singleObject();
	private Logger logger = LoggerFactory.getLogger(EnoughPlayerRegistered.class);
	
	public EnoughPlayerRegistered() {	}
	
	@Override
	public void newGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerPlayer(String gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendHalfMap(String gameID, HalfMap halfMap) {
			List<Player> playerList = database.getGameByID(gameID).getPlayers();
			enoughPlayersRegistered(playerList, gameID, halfMap.getUniquePlayerID());
	
	}

	@Override
	public void gameState(String gameID, String playerID) {
		
	}

	public void enoughPlayersRegistered(List<Player> list, String gameID, String uniquePlayerID) {
		LocalGameState localGameState = new LocalGameState();
		if(list.size() != 2) {
			localGameState.setLostOrWon(list, gameID, uniquePlayerID);
			logger.error("There were no 2 registered players for the client to send a map!");
			throw new ServerException("Map sending", "Not 2 players registered!");
		}
	}
}
