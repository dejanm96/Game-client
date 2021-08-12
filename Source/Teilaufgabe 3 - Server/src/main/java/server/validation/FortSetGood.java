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

public class FortSetGood implements IRule{

	private MapConverter mapConverter = new MapConverter();
	private Logger logger = LoggerFactory.getLogger(FortSetGood.class);
	private GameDatabase database = GameDatabase.singleObject();
	public FortSetGood() {	}
	
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
		List<Node> convertedMap = mapConverter.convertServerToClientMap(halfMap.getNodes());
		fortPlacedOnGrass(convertedMap, gameID, halfMap.getUniquePlayerID());
	}

	@Override
	public void gameState(String gameID, String playerID) {
		// TODO Auto-generated method stub
		
	}

	
	
	private void fortPlacedOnGrass(List<Node> halfmap, String gameId, String uniquePlayerID) {
		LocalGameState localGameState = new LocalGameState();
		boolean fortFound = false;
		int fortCounter = 0;
		for (Node node : halfmap) {
			if(node.isFortPresent())
				fortCounter++;
			
			if (node.isFortPresent() && node.getType().equals(NodeType.GRASS)) {
				fortFound = true;
			}
		}
		if (!fortFound || fortCounter > 1) {
			localGameState.setLostOrWon(database.getGameByID(gameId).getPlayers() ,gameId, uniquePlayerID);
			logger.error("FORT NOT FOUND!");
			throw new ServerException("Business rule broken", "Mistake with fort placing!");
		}
	}

}
