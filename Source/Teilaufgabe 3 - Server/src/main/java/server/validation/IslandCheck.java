package server.validation;

import java.util.ArrayList;
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

public class IslandCheck implements IRule {

	private List<Node> halfmap;
	private List<Node> mapOfWalkableNodes;
	private int numberOfWalkableNodes = 0;
	private MapConverter mapConverter = new MapConverter();
	private GameDatabase database = GameDatabase.singleObject();
	private Logger logger = LoggerFactory.getLogger(IslandCheck.class);
	
	public IslandCheck() {
		this.mapOfWalkableNodes = new ArrayList<>();
	}

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
		LocalGameState localGameState = new LocalGameState();
		this.halfmap = mapConverter.convertServerToClientMap(halfMap.getNodes());
		calculateNoOfWalkableNodes();
		mapOfWalkableNodes.clear();
		
		Node startNode = findStartNode();
		checkMapForIsland(startNode.getX(), startNode.getY());
		if(mapOfWalkableNodes.size() != numberOfWalkableNodes) {
			localGameState.setLostOrWon(database.getGameByID(gameID).getPlayers() ,gameID, halfMap.getUniquePlayerID());
			logger.error("There was an Island on the map!");
			throw new ServerException("Business rule broken", "An island has bin built!");
		}
	}

	@Override
	public void gameState(String gameID, String playerID) {
		// TODO Auto-generated method stub
		
	}


	private void checkMapForIsland(int x, int y) {

		int save = 0;

		if (x < 0 || x > 7 || y < 0 || y > 3)
			return;

		for (Node n : mapOfWalkableNodes)
			if (n.getX() == x && n.getY() == y)
				return;

		for (int i = 0; i < halfmap.size(); i++) {
			if (halfmap.get(i).getX() == x && halfmap.get(i).getY() == y
					&& halfmap.get(i).getType().equals(NodeType.WATER)) {
				return;

			} else if (halfmap.get(i).getX() == x && halfmap.get(i).getY() == y)
				save = i;
		}
		mapOfWalkableNodes.add(halfmap.get(save));

		checkMapForIsland(x + 1, y);
		checkMapForIsland(x - 1, y);
		checkMapForIsland(x, y + 1);
		checkMapForIsland(x, y - 1);
	}

	private Node findStartNode() {
		Node startNode = null;
		for (Node node : halfmap)
			if (node.isFortPresent())
				startNode = new Node(node.getX(), node.getY());
		return startNode;
	}

	private void calculateNoOfWalkableNodes() {
		numberOfWalkableNodes = 0;
		for (Node node : halfmap)
			if (node.getType().equals(NodeType.GRASS) || node.getType().equals(NodeType.MOUNTAIN))
				numberOfWalkableNodes++;
	}

}
