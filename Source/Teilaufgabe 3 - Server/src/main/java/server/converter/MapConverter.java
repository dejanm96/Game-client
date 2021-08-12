package server.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import MessagesBase.ETerrain;
import MessagesBase.HalfMapNode;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMapNode;
import server.enums.FortState;
import server.enums.NodeType;
import server.enums.PlayerGameState;
import server.enums.PlayerPositionState;
import server.enums.TreasureState;
import server.game.Player;
import server.map.Node;


public class MapConverter {
	
	public MapConverter() {  }
	
	public List<Node> convertServerToClientMap(Collection<HalfMapNode> halfMap) {
		// convert the map into my map by calling the constructor of Node class
		List<Node> listToGiveBack = new ArrayList<>();

		for (HalfMapNode h : halfMap) {
			listToGiveBack.add(new Node(h.getX(), h.getY()));
		}

		/**
		 * Setting all the other variables except X and Y on each coordinate done with
		 * switch case on every variable by simply setting every variable.
		 */
		for (HalfMapNode h : halfMap) {
			for (Node n : listToGiveBack) {
				if (h.getX() == n.getX() && h.getY() == n.getY()) {
					if(h.isFortPresent()) {
						n.setFortstate(FortState.MyFortPresent);
						n.setFortPresent(true);
					}else
						n.setFortstate(FortState.NoOrUnknownFortState);
					
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
				}
			}
		}
		listToGiveBack.sort(Comparator.comparing(Node::getX));
		listToGiveBack.sort(Comparator.comparing(Node::getY));

		return listToGiveBack;
	}
	
	public Collection<FullMapNode> convertClientToServerMap(List<Node> nodeList, List<Player> playerList, String playerID) {
		Collection<FullMapNode> nodes = new ArrayList<>();
		ConvertLocalEnumsToServerEnums enumConverter = new ConvertLocalEnumsToServerEnums();
		
		Player currentPlayer = getPlayerFromList(playerList, playerID);
		setPlayerPosition(nodeList);
		for(Node node : nodeList) {
			if(node.getFortstate().equals(FortState.MyFortPresent) && !(currentPlayer.getCastlePosition().getX() == node.getX()
					&& currentPlayer.getCastlePosition().getY() == node.getY())) {
				node.setFortstate(FortState.NoOrUnknownFortState);
				node.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
			}
		
			if(node.getTreasureState().equals(TreasureState.MyTreasureIsPresent) && !getPlayerFromList(playerList, playerID).getTreasureCollected())
				node.setTreasureState(TreasureState.NoOrUnknownTreasureState);
			
			if (node.getType().equals(NodeType.GRASS)) {
				nodes.add(new FullMapNode((ETerrain.Grass), enumConverter.convertPlayerPositionState(node.getPlayerPositionState()), 
						enumConverter.convertTreasureState(node.getTreasureState()), enumConverter.convertFortState(node.getFortstate()), node.getX(), node.getY()));
			}
			if (node.getType().equals(NodeType.WATER)) {
				nodes.add(new FullMapNode((ETerrain.Water), enumConverter.convertPlayerPositionState(node.getPlayerPositionState()), 
						enumConverter.convertTreasureState(node.getTreasureState()), enumConverter.convertFortState(node.getFortstate()), node.getX(), node.getY()));
			}
			if (node.getType().equals(NodeType.MOUNTAIN)) {
				nodes.add(new FullMapNode((ETerrain.Mountain), enumConverter.convertPlayerPositionState(node.getPlayerPositionState()), 
						enumConverter.convertTreasureState(node.getTreasureState()), enumConverter.convertFortState(node.getFortstate()), node.getX(), node.getY()));
			}
			
		}
		return nodes;
	}
	
	private void setPlayerPosition(List<Node> nodelist) {
		Random random = new Random();
		nodelist.get(random.nextInt(64)).setPlayerPositionState(PlayerPositionState.EnemyPlayerPosition);
	}
	
	public Player getPlayerFromList(List<Player> playerList, String playerID) {
		Player result = null;
		for(Player player : playerList)
			if(player.getUniquePlayerID().equals(playerID))
				result = player;
		return result;
	}
	
	public EPlayerGameState convertLocalPlayerGameStateToServer(PlayerGameState playerGameState) {
		EPlayerGameState result = null;
		
		switch (playerGameState) {
		case Lost:
			result = EPlayerGameState.Lost;
			break;
		case Won:
			result = EPlayerGameState.Won;
			break;
		case ShouldActNext:
			result = EPlayerGameState.ShouldActNext;
			break;
		case ShouldWait:
			result = EPlayerGameState.ShouldWait;
			break;
		default:
			break;
		}
		
		return result;
	}
}
