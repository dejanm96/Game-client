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

public class WaterOnEdgesCheck implements IRule{

	private MapConverter mapConverter = new MapConverter();
	private Logger logger = LoggerFactory.getLogger(WaterOnEdgesCheck.class);
	private GameDatabase database = GameDatabase.singleObject();
	
	public WaterOnEdgesCheck() {  }
	
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
		checkWaterOnMapEdges(mapConverter.convertServerToClientMap(halfMap.getNodes()), gameID, halfMap.getUniquePlayerID());
		
	}

	@Override
	public void gameState(String gameID, String playerID) {
		// TODO Auto-generated method stub
		
	}
	
	private void checkWaterOnMapEdges(List<Node> halfmap, String gameId, String uniquePlayerID) {
		LocalGameState localGameState = new LocalGameState();
		int leftWater = 0, rightWater = 0, topWater = 0, bottomWater = 0;
		for(Node node : halfmap) {
			if(node.getX() == 0 && node.getType().equals(NodeType.WATER)) {
				leftWater++;
			}else
			if(node.getX() == 7 && node.getType().equals(NodeType.WATER)) {
				rightWater++;
			}else
			if(node.getY() == 0 && node.getType().equals(NodeType.WATER)) {
				topWater++;
			}else
			if(node.getY() == 3 && node.getType().equals(NodeType.WATER)) {
				bottomWater++;
			}
		}
		
		if(leftWater > 1 || rightWater > 1 || topWater > 3 || bottomWater > 3) {
			localGameState.setLostOrWon(database.getGameByID(gameId).getPlayers() ,gameId, uniquePlayerID);
			logger.error("There was more water on the edges!");
			throw new ServerException("Business rules broken", "The map has more then a half water fields on one or more edge!");
		}
		
	}
	

	
	
}
