package client.helpers;

import java.util.ArrayList;
import java.util.List;

import client.map.enums.NodeType;
import client.map.models.Node;

public class MapValidator {
	private List<Node> nodesToCheck;
	private List<Node> mapOfWalkableNodes;
	
	
	private static final int WALKABLE_NODES = 27;

	public MapValidator(List<Node> list) {
		this.nodesToCheck = list;
		this.mapOfWalkableNodes = new ArrayList<>();
	}
	
	/**
	 * Here all the single water Nodes are counted on all 4 sides
	 * If there are more then 1 water nodes on left and right side 
	 * Or more then 4 nodes on top and bottom side false is returned
	 * and a new map will be made in the MapHalf Class
	 * @return good
	 */
	public boolean validation() {
		

		int numberOfCastleNode = 0;
		for (int i = 0; i < nodesToCheck.size(); i++) {
			if(nodesToCheck.get(i).isFortPresent())
				numberOfCastleNode = i;
		} 
		
		checkMapForIsland(nodesToCheck.get(numberOfCastleNode).getX(), nodesToCheck.get(numberOfCastleNode).getY());
		
		return checkMapForWaterOnEdges() && (mapOfWalkableNodes.size() == WALKABLE_NODES);
		
	}
	/**
	 * Checking if the map edges are legal.
	 * @return boolean good
	 */
	private boolean checkMapForWaterOnEdges() {
		boolean good = false;
		int leftWater = 0, rightWater = 0, topWater = 0, bottomWater = 0;
		for(Node n : nodesToCheck) {
			if(n.getX() == 0 && n.getType().equals(NodeType.WATER)) {
				leftWater++;
			}
		}
		for(Node n : nodesToCheck) {
			if(n.getX() == 7 && n.getType().equals(NodeType.WATER)) {
				rightWater++;
			}
		}
		for(Node n : nodesToCheck) {
			if(n.getY() == 0 && n.getType().equals(NodeType.WATER)) {
				topWater++;
			}
		}
		for(Node n : nodesToCheck) {
			if(n.getY() == 3 && n.getType().equals(NodeType.WATER)) {
				bottomWater++;
			}
		}
		if(leftWater <= 1 && rightWater <= 1 && topWater <= 3 && bottomWater <= 3) {
			good = true;
		}		
		return good;
	}
	
	
	
	/**
	 *The "islandCheck" function is implemented with help of the floodfill algorithm found on the 
	 *geeksforgeeks page https://www.geeksforgeeks.org/flood-fill-algorithm-implement-fill-paint/
	 *row 87 - row 111
	 *
	 *First its to be checked if x and y are larger then the map should be if yes, just return
	 *If the node is already visited also return. If the node which is checket atm is water
	 *also return. Else the node is put in a new list and the function is called again 
	 *for right left top and bottom recursively.
	 */
	private void checkMapForIsland(int x, int y) {
		
		int save = 0;

		if (x < 0 || x > 7 || y < 0 || y > 3)
			return;
		
		for(Node n : mapOfWalkableNodes)
			if(n.getX() == x && n.getY() == y) 
				return;
			
		for(int i = 0; i < nodesToCheck.size(); i++) {
			if(nodesToCheck.get(i).getX() == x && nodesToCheck.get(i).getY() == y && nodesToCheck.get(i).getType().equals(NodeType.WATER)) {
				return;

			}else if(nodesToCheck.get(i).getX() == x && nodesToCheck.get(i).getY() == y)
				save = i;
		}
		mapOfWalkableNodes.add(nodesToCheck.get(save));
		
		checkMapForIsland(x + 1, y);
		checkMapForIsland(x - 1, y);
		checkMapForIsland(x, y + 1);
		checkMapForIsland(x, y - 1);
	}
	
	
	
	
	public List<Node> getmapOfWalkableNodes() {
		return mapOfWalkableNodes;
	}
	
}
