package server.wholemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.enums.FortState;
import server.enums.NodeType;
import server.enums.PlayerPositionState;
import server.enums.TreasureState;
import server.game.Player;
import server.map.Node;

public class CombineMapHalfs {

	private List<Player> playerList;
	
	private List<Node> firstMapHalf = new ArrayList<>();
	private List<Node> secondMapHalf = new ArrayList<>();
	
	public CombineMapHalfs(List<Player> players) { 
		playerList = players;
		firstMapHalf = players.get(0).getPlayerHalfMap();
		secondMapHalf = players.get(1).getPlayerHalfMap();		
		
		setTreasure(firstMapHalf);
		setTreasure(secondMapHalf);
	}

	private void setTreasure(List<Node> mapHalf) {
		boolean treasureSet = false;
		
		while(!treasureSet) {
			int randomNo = new Random().nextInt(mapHalf.size());
			if(mapHalf.get(randomNo).getType().equals(NodeType.GRASS)) {
				mapHalf.get(randomNo).setTreasureState(TreasureState.MyTreasureIsPresent);
				treasureSet = true;
			}
		}
			
		for(Node node : mapHalf)
			if(node.getTreasureState() == null)
				node.setTreasureState(TreasureState.NoOrUnknownTreasureState);
		
	}

	public List<Node> randomCombineMapHalfs() {

		List<Node> wholeMap = new ArrayList<>();
		Random random = new Random();
		int getNextNumber = random.nextInt(4);
	
		if(getNextNumber == 0) {
			wholeMap = combineMapsHorisontaly(firstMapHalf, secondMapHalf, 0, 1);
		}else if(getNextNumber == 1) {
			wholeMap = combineMapsHorisontaly(secondMapHalf, firstMapHalf, 1, 0);
		}else if(getNextNumber == 2) {
			wholeMap = combineMapsVerticaly(firstMapHalf, secondMapHalf, 0, 1);
		}else if(getNextNumber == 3) {
			wholeMap = combineMapsVerticaly(secondMapHalf, firstMapHalf, 1, 0);
		}
		
		return wholeMap;
	}
	
	private List<Node> combineMapsHorisontaly(List<Node> firstMap, List<Node> secondMap, int playerPosition1, int playerPosition2) {
		List<Node> resultMap = new ArrayList<>();
		for(Node node : firstMap) {
			if(node.getFortstate().equals(FortState.MyFortPresent)){
				node.setPlayerPositionState(PlayerPositionState.MyPosition);
				node.setFortstate(FortState.MyFortPresent);
				playerList.get(playerPosition1).setCastlePosition(new Node(node.getX(), node.getY()));
			}else 
				node.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
			resultMap.add(node);
		}
		for(Node node : secondMap) {
				node.setY(node.getY() + 4);
				if(node.getFortstate().equals(FortState.MyFortPresent)) {
					node.setPlayerPositionState(PlayerPositionState.MyPosition);
					node.setFortstate(FortState.MyFortPresent);
					playerList.get(playerPosition2).setCastlePosition(new Node(node.getX(), node.getY()));
				}else {node.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
					node.setFortstate(FortState.NoOrUnknownFortState);
				}
				resultMap.add(node);
		}

		return resultMap;
	}
	
	private List<Node> combineMapsVerticaly(List<Node> firstMap, List<Node> secondMap, int playerPosition1, int playerPosition2) {
		List<Node> resultMap = new ArrayList<>();
		for(Node node : firstMap) {
			if(node.getFortstate().equals(FortState.MyFortPresent)) {
				node.setPlayerPositionState(PlayerPositionState.MyPosition);
				node.setFortstate(FortState.MyFortPresent);
				playerList.get(playerPosition1).setCastlePosition(new Node(node.getX(), node.getY()));
			}
			else
				node.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
			resultMap.add(node);
		}
		for(Node node : secondMap) {
				node.setX(node.getX() + 8);				
				if(node.getFortstate().equals(FortState.MyFortPresent)) {
					node.setPlayerPositionState(PlayerPositionState.MyPosition);
					node.setFortstate(FortState.MyFortPresent);
					playerList.get(playerPosition2).setCastlePosition(new Node(node.getX(), node.getY()));
				}else {node.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
					node.setFortstate(FortState.NoOrUnknownFortState);
				}
				resultMap.add(node);
		}

		return resultMap;
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}
	
}
