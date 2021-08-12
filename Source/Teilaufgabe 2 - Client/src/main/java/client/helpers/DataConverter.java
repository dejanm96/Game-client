package client.helpers;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import MessagesBase.EMove;
import MessagesBase.ETerrain;
import MessagesBase.HalfMapNode;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMapNode;
import client.map.enums.Direction;
import client.map.enums.FortState;
import client.map.enums.NodeType;
import client.map.enums.PlayerGameState;
import client.map.enums.PlayerPositionState;
import client.map.enums.TreasureState;
import client.map.models.FullMap;
import client.map.models.Node;

/**
 * Data Converter converts the map from server to client and backwards,
 * converts the server Enums to local enums, the move from string to 
 * server understandable enum and the string to print in the view.
 * @author Dejan Micic
 *
 */
public class DataConverter {

	public DataConverter() { }

	/**
	 * Converting the client map to the server understandable map.
	 * @param map
	 * @return Collection<HalfMapNode> nodes
	 */
	public Collection<HalfMapNode> convertClientToServerMap(FullMap map) {
		Collection<HalfMapNode> nodes = new ArrayList<>();

		for (int i = 0; i < map.getMapNodes().size(); i++) {
			if (map.getMapNodes().get(i).getType().equals(NodeType.GRASS)) {
				nodes.add(new HalfMapNode(map.getMapNodes().get(i).getX(), map.getMapNodes().get(i).getY(),
						map.getMapNodes().get(i).isFortPresent(), ETerrain.Grass));
			}
			if (map.getMapNodes().get(i).getType().equals(NodeType.WATER)) {
				nodes.add(new HalfMapNode(map.getMapNodes().get(i).getX(), map.getMapNodes().get(i).getY(),
						map.getMapNodes().get(i).isFortPresent(), ETerrain.Water));
			}
			if (map.getMapNodes().get(i).getType().equals(NodeType.MOUNTAIN)) {
				nodes.add(new HalfMapNode(map.getMapNodes().get(i).getX(), map.getMapNodes().get(i).getY(),
						map.getMapNodes().get(i).isFortPresent(), ETerrain.Mountain));
			}
		}
		return nodes;
	}

	/**
	 * Converting the map gotten from the server into the locally understandable map
	 * @param fullMap
	 * @return List<Node> listToGiveBack
	 */
	public List<Node> convertServerToClientMap(Collection<FullMapNode> fullMap) {
		// convert the map into my map by calling the constructor of Node class
		List<Node> listToGiveBack = new ArrayList<>();

		for (FullMapNode h : fullMap) {
			listToGiveBack.add(new Node(h.getX(), h.getY()));
		}

		/**
		 * Setting all the other variables except X and Y on each coordinate done with
		 * switch case on every variable by simply setting every variable.
		 */
		for (FullMapNode h : fullMap) {
			for (Node n : listToGiveBack) {
				if (h.getX() == n.getX() && h.getY() == n.getY()) {
					switch (h.getFortState()) {
					case EnemyFortPresent:
						n.setFortstate(FortState.EnemyFortPresent);
						break;
					case NoOrUnknownFortState:
						n.setFortstate(FortState.NoOrUnknownFortState);
						break;
					case MyFortPresent:
						n.setFortstate(FortState.MyFortPresent);
						break;
					default:
						break;
					}
					switch (h.getTerrain()) {
					case Grass:
						n.setType(NodeType.GRASS);
						break;
					case Mountain:
						n.setType(NodeType.MOUNTAIN);
						break;
					case Water:
						n.setType(NodeType.WATER);
					default:
						break;
					}
					switch (h.getPlayerPositionState()) {
					case BothPlayerPosition:
						n.setPlayerPositionState(PlayerPositionState.BothPlayerPosition);
						break;
					case EnemyPlayerPosition:
						n.setPlayerPositionState(PlayerPositionState.EnemyPlayerPosition);
						break;
					case MyPosition:
						n.setPlayerPositionState(PlayerPositionState.MyPosition);
						break;
					case NoPlayerPresent:
						n.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
						break;
					default:
						break;
					}
					switch (h.getTreasureState()) {
					case MyTreasureIsPresent:
						n.setTreasureState(TreasureState.MyTreasureIsPresent);
						break;
					case NoOrUnknownTreasureState:
						n.setTreasureState(TreasureState.NoOrUnknownTreasureState);
						break;
					default:
						break;
					}
				}
			}
		}
		listToGiveBack.sort(Comparator.comparing(Node::getX));
		listToGiveBack.sort(Comparator.comparing(Node::getY));

		return listToGiveBack;
	}

	/**
	 * Converting the Server enum into locall enums.
	 * @param state
	 * @return PlayerGameState playerState
	 */
	public PlayerGameState getPlayerGameState(EPlayerGameState state) {

		PlayerGameState playerState = null;

		switch (state) {
		case ShouldActNext:
			playerState = PlayerGameState.ShouldActNext;
			break;
		case ShouldWait:
			playerState = PlayerGameState.ShouldWait;
			break;
		case Won:
			playerState = PlayerGameState.Won;
			break;
		case Lost:
			playerState = PlayerGameState.Lost;
			break;
		}

		return playerState;
	}
	
	/**
	 * Converting moves from string to Enum understandable for the server before sending.
	 * @param way
	 * @return EMove move
	 */
	public EMove convertMove(Direction way) {
		EMove move = null;
		switch (way) {
		case DOWN:
			move = EMove.Down;
			break;

		case UP:
			move = EMove.Up;
			break;

		case RIGHT:
			move = EMove.Right;
			break;

		case LEFT:
			move = EMove.Left;
			break;

		default:
			break;

		}
		return move;
	}

	/**
	 * Converting the nodeType into a String as a helper for the view class.
	 * @param type
	 * @return String typeToString
	 */
	public String typeConverter(NodeType type) {
		String typeToString = "";

		switch (type) {
		case GRASS:
			typeToString = "GRS";
			break;
		case MOUNTAIN:
			typeToString = "MNT";
			break;
		case WATER:
			typeToString = "WTR";
			break;
		default:
			break;
		}
		return typeToString;
	}

}
