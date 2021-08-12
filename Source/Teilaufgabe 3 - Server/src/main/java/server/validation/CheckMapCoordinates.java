package server.validation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import server.converter.MapConverter;
import server.database.GameDatabase;
import server.exceptions.ServerException;
import server.gamestate.LocalGameState;
import server.map.Node;

public class CheckMapCoordinates implements IRule {

	private MapConverter mapConverter = new MapConverter();
	private Logger logger = LoggerFactory.getLogger(CheckMapCoordinates.class);
	private GameDatabase database = GameDatabase.singleObject();
	
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
		checkMapCoordinates(mapConverter.convertServerToClientMap(halfMap.getNodes()), gameID, halfMap.getUniquePlayerID());

	}

	@Override
	public void gameState(String gameID, String playerID) {
		// TODO Auto-generated method stub

	}

	private void checkMapCoordinates(List<Node> halfmap, String gameId, String uniquePlayerID) {
		LocalGameState localGameState = new LocalGameState();
		for(Node node : halfmap)
			if(node.getX() > 7 || node.getY() > 3) {
				localGameState.setLostOrWon(database.getGameByID(gameId).getPlayers(), gameId, uniquePlayerID);
				logger.error("The map coordinates are not good!");
				throw new ServerException("Map business rule", "The map coordinates are not good !");
			}
	}
	
}
