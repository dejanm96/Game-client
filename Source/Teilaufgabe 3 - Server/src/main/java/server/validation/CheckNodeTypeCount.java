package server.validation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import server.converter.MapConverter;
import server.database.GameDatabase;
import server.enums.NodeType;
import server.exceptions.ServerException;
import server.gamestate.LocalGameState;
import server.map.Node;

public class CheckNodeTypeCount implements IRule{
	
	private MapConverter mapConverter = new MapConverter();
	private Logger logger = LoggerFactory.getLogger(CheckNodeTypeCount.class);
	private GameDatabase database = GameDatabase.singleObject();
	public CheckNodeTypeCount() {	}
	
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
		checkGrassFieldCount(mapConverter.convertServerToClientMap(halfMap.getNodes()), gameID, halfMap.getUniquePlayerID());
		
	}

	@Override
	public void gameState(String gameID, String playerID) {
		// TODO Auto-generated method stub
		
	}
	
	private void checkGrassFieldCount(List<Node> halfmap, String gameId, String uniquePlayerID) {
		
		int grassFieldCounter = 0;
		int mountainFieldCounter = 0;
		int waterFieldCounter = 0;
		
		for (Node node : halfmap) {
			if (node.getType().equals(NodeType.GRASS))
				grassFieldCounter++;
			else if(node.getType().equals(NodeType.MOUNTAIN))
				mountainFieldCounter++;
			else if(node.getType().equals(NodeType.WATER))
				waterFieldCounter++;		
		}
		
		if (grassFieldCounter < 15 || mountainFieldCounter < 3 || waterFieldCounter < 4) {
			LocalGameState localGameState = new LocalGameState();
			logger.error("The map has not enough of some Node Types!");
			localGameState.setLostOrWon(database.getGameByID(gameId).getPlayers() ,gameId, uniquePlayerID);
			throw new ServerException("Business rules broken", "The map has not enough of some Node Types!");
		}
	}	

	

}
