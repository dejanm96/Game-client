package client.map.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import client.map.enums.FortState;
import client.map.enums.NodeType;
import client.map.enums.PlayerPositionState;
import client.map.enums.TreasureState;
/**
 * The map is being built of Nodes. Every node has coordinates x and y, 
 * a type and players are walking over the nodes in search of the treasure
 * and the enemy fort.
 * @author Dejan Micic
 *
 */
public class Node {
	private NodeType type;
	private PlayerPositionState playerPositionState;
	private TreasureState treasureState;
	private FortState fortState;
	
	private boolean fortPresent;
	private int X = 0; 
	private int Y = 0;

	private boolean visited; 

	private int distance = Integer.MAX_VALUE;
	
	private List<Node> shortestPath = new LinkedList<>();
	Map<Node, Integer> adjacentNodes = new HashMap<>();
	private List<Node> setteldNodes = new LinkedList<>();

	
	public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

	
	public Node(int X, int Y) {
		this.X = X;
		this.Y = Y;
		this.fortPresent = false;
		this.visited = false;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public PlayerPositionState getPlayerPositionState() {
		return playerPositionState;
	}

	public void setPlayerPositionState(PlayerPositionState playerPositionState) {
		this.playerPositionState = playerPositionState;
	}

	public TreasureState getTreasureState() {
		return treasureState;
	}

	public void setTreasureState(TreasureState treasureState) {
		this.treasureState = treasureState;
	}

	public FortState getFortstate() {
		return fortState;
	}

	public void setFortstate(FortState fortstate) {
		this.fortState = fortstate;
	}

	public boolean isFortPresent() {
		return fortPresent;
	}

	public void setFortPresent(boolean fortPresent) {
		this.fortPresent = fortPresent;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
	
	public void visit() {
		this.visited = true;
	}
	
	public void unvisit() {
		this.visited = false;
	}
	
	
	
	public boolean isVisited() {
		return visited;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + X;
		result = prime * result + Y;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (X != other.X)
			return false;
		if (Y != other.Y)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Map<Node, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public List<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestPath) {
		this.shortestPath.addAll(shortestPath);
	}

	public List<Node> getSetteldNodes() {
		return setteldNodes;
	}

	public void setSetteldNodes(List<Node> setteldNodes) {
		this.setteldNodes = setteldNodes;
	}

	
	
	@Override
	public String toString() {
		return "Node [type=" + type + ", X=" + X + ", Y=" + Y + "]";
	}
	
	
	

}
