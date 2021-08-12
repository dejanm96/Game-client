package server.map;

import server.enums.FortState;
import server.enums.NodeType;
import server.enums.PlayerPositionState;
import server.enums.TreasureState;

public class Node {
	private NodeType type;
	private FortState fortState;
	private PlayerPositionState playerPositionState;
	private TreasureState treasureState;

	private boolean fortPresent;
	private int X = 0; 
	private int Y = 0;
	
	public Node(int X, int Y) {
		this.X = X;
		this.Y = Y;
		this.fortPresent = false;
	}
	public Node(NodeType type, FortState fortState, PlayerPositionState playerPositionState,
			TreasureState treasureState, boolean fortPresent, int x, int y) {
		super();
		this.type = type;
		this.fortState = fortState;
		this.playerPositionState = playerPositionState;
		this.treasureState = treasureState;
		this.fortPresent = fortPresent;
		X = x;
		Y = y;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
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
	
	
	@Override
	public String toString() {
		return "Node [type=" + type + ", X=" + X + ", Y=" + Y + "]";
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
	
	
	

}
