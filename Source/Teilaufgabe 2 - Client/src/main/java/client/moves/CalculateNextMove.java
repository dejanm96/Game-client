package client.moves;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;

import client.map.enums.Direction;
import client.map.enums.FortState;
import client.map.enums.KindOfSearch;
import client.map.enums.NodeType;
import client.map.enums.PlayerPositionState;
import client.map.enums.TreasureState;
import client.map.models.Node;


/**
 * CalculateNextMove is a class with the logic of the movement of the players. Class firstly calculates
 * all distances and shortestPaths from the current node to every single node in the map from which is then determined
 * which node will be visited next.
 * @author Dejan Micic
 *
 */
public class CalculateNextMove {
	private List<Node> wholeMap = new ArrayList<>();
	private Node current = null;

	//list of visited nodes
	private List<Node> visited = new ArrayList<>();

	private List<Node> myHalfList = new ArrayList<>();
	private List<Node> enemyHalf = new ArrayList<>();

	//list of shortest path nodes that have to be visited to come to destination node
	private List<Node> listOfShortestPath = new ArrayList<>();

	public CalculateNextMove() {  }

	/**
	 * Calculates the next move that should be done
	 * Gets the kindOfSearch which can be treasure or fort depending on which part
	 * of the game we are at the moment.
	 * Returns a map of the MOVE in which the player is moving and an integer, which
	 * represents the number, how often should one move be sent depending on the 
	 * current and destination node.
	 * @param kindOfSearch
	 * @return 
	 */
	public Map<Direction, Integer> calculateNextMove(KindOfSearch kindOfSearch) {

		Node newNode = null;

		calculateShortestPathFromSource();

		//Sorting the map on distance so the smallest will always be first
		wholeMap.sort(Comparator.comparing(Node::getDistance));
		if(myHalfList.isEmpty())
			findMyHalf();
		if(enemyHalf.isEmpty())
			setEnemyHalfList();


		//If the listOfShortestPath is empty we shoud calculate the next destination node again
		if (listOfShortestPath.size() == 0) {
			switch (kindOfSearch) {
			case TREASURE:
				if (visited.isEmpty())
					visited.add(current);
				//if we are on a mountain, all surrounding nodes are checked for treasure
				//if treasure was found, the node is set as next destination
				if (current.getType().equals(NodeType.MOUNTAIN) && checkMountainNeighbours(KindOfSearch.TREASURE) != null
						&& myHalfList.contains(checkMountainNeighbours(KindOfSearch.TREASURE))) {
					newNode = checkMountainNeighbours(KindOfSearch.TREASURE);
					visited.add(newNode);
					listOfShortestPath.addAll(newNode.getShortestPath());
					listOfShortestPath.add(newNode);
				} else {
					newNode = getNextNode(myHalfList);
				}
				break;

			case FORT:

				if (current.getType().equals(NodeType.MOUNTAIN) && checkMountainNeighbours(KindOfSearch.FORT) != null
						&& enemyHalf.contains(checkMountainNeighbours(KindOfSearch.FORT))) {
					
					newNode = checkMountainNeighbours(KindOfSearch.FORT);
					visited.add(newNode);
					listOfShortestPath.addAll(newNode.getShortestPath());
					listOfShortestPath.add(newNode);
				} else {
					newNode = getNextNode(enemyHalf);
				}
				break;

			default:
				System.out.println("Wrong kind of search!");
				break;
			}
		}
			
		//the list of path always contains the current node, this is first removed
		if (listOfShortestPath.contains(current)) {
			listOfShortestPath.remove(current);
		}

		//finding the first item in the list and setting it as next node 
		if (listOfShortestPath.size() > 0) {
			for (Node n : listOfShortestPath) {
				newNode = n;

				listOfShortestPath.remove(n);
				break;
			}
		}

		Map<Direction, Integer> way = checkSideOdAdijacent(current, newNode);
		current = newNode;
		return way;

	}

	//Getting the next node and getting the shortest path to the next node
	private Node getNextNode(List<Node> half) {
		Node nodeToReturn = null;
		for (Node n : wholeMap) {
			if (!visited.contains(n) && half.contains(n)) {
				nodeToReturn = n;
				visited.add(nodeToReturn);
				listOfShortestPath.addAll(nodeToReturn.getShortestPath());
				listOfShortestPath.add(n);
				break;
			}
		}
		return nodeToReturn;
	}

	
	
	/**
	 * The next 3 functions are from the page: 
	 * https://www.codeflow.site/de/article/java-dijkstra?fbclid=IwAR0zB7zb3tRqKTyMLbyRddz_lVbaIEovVGqz_eHyY9uaqCJaX77niuvl2LA#_2_krzester_pfad_mit_dijkstra
	 * Lines 153 - 199
	 * CalculateShortestPathFromSource calculates the shortest path from source to every other Node.
	 * @return wholeMap but with distances for the Nodes set
	 */
	private List<Node> calculateShortestPathFromSource() {
		current.setDistance(0);

		Set<Node> settledNodes = new HashSet<>();
		Set<Node> unsettledNodes = new HashSet<>();

		unsettledNodes.add(current);

		while (unsettledNodes.size() != 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			unsettledNodes.remove(currentNode);
			for (Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
				Integer edgeWeight = adjacencyPair.getValue();
				if (!settledNodes.contains(adjacentNode)) {
					CalculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
					unsettledNodes.add(adjacentNode);
				}
			}
			settledNodes.add(currentNode);
		}
		return wholeMap;
	}

	private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
		Node lowestDistanceNode = null;
		int lowestDistance = Integer.MAX_VALUE;
		for (Node node : unsettledNodes) {
			int nodeDistance = node.getDistance();
			if (nodeDistance < lowestDistance) {
				lowestDistance = nodeDistance;
				lowestDistanceNode = node;
			}
		}
		return lowestDistanceNode;
	}

	private static void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
		Integer sourceDistance = sourceNode.getDistance();

		if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
			evaluationNode.setDistance(sourceDistance + edgeWeigh);
			LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
			shortestPath.add(sourceNode);
			evaluationNode.setShortestPath(shortestPath);
		}
	}
	//*****************************************************************************************************************
	
	//checking every 8 sorrounding nodes for treasure because they can be seen of a mountain.
	public Node checkMountainNeighbours(KindOfSearch kindOfSearch) {
		Node node = null;
	
		if(kindOfSearch.equals(KindOfSearch.FORT)) {
			Node x = legalNode(current.getX() - 1, current.getY());
			node = checkNodeForTreasure(x);
	
			x = legalNode(current.getX() + 1, current.getY());
			node = checkNodeForTreasure(x);
	
			x = legalNode(current.getX(), current.getY() - 1);
			node = checkNodeForTreasure(x);
	
			x = legalNode(current.getX(), current.getY() + 1);
			node = checkNodeForTreasure(x);
			
			x = legalNode(current.getX() + 1, current.getY() + 1);
			node = checkNodeForTreasure(x);
			
			x = legalNode(current.getX() - 1, current.getY() - 1);
			node = checkNodeForTreasure(x);
			
			x = legalNode(current.getX() + 1, current.getY() - 1);
			node = checkNodeForTreasure(x);
			
			x = legalNode(current.getX() - 1, current.getY() + 1);
			node = checkNodeForTreasure(x);
		}
		else {
			if(kindOfSearch.equals(KindOfSearch.FORT)) {
				Node x = legalNode(current.getX() - 1, current.getY());
				node = checkNodeForEnemyFort(x);
		
				x = legalNode(current.getX() + 1, current.getY());
				node = checkNodeForEnemyFort(x);
		
				x = legalNode(current.getX(), current.getY() - 1);
				node = checkNodeForEnemyFort(x);
		
				x = legalNode(current.getX(), current.getY() + 1);
				node = checkNodeForEnemyFort(x);
				
				x = legalNode(current.getX() + 1, current.getY() + 1);
				node = checkNodeForEnemyFort(x);
				
				x = legalNode(current.getX() - 1, current.getY() - 1);
				node = checkNodeForEnemyFort(x);
				
				x = legalNode(current.getX() + 1, current.getY() - 1);
				node = checkNodeForEnemyFort(x);
				
				x = legalNode(current.getX() - 1, current.getY() + 1);
				node = checkNodeForEnemyFort(x);
			}
		}
		return node;
	}
	
	//Checking if its a legal node, if it contains treasure its returned if not its marked as visited
	private Node checkNodeForTreasure(Node x) {
		Node nodeToReutrn = null;
		if (x != null) {
			if(x.getTreasureState().equals(TreasureState.MyTreasureIsPresent))
				nodeToReutrn = x;
			else {
				if(myHalfList.contains(x)){
				visited.add(x);
				}
			}
		}
		return nodeToReutrn;
	}
	
	//Checking if its a legal node, if it contains enemy fort its returned if not its marked as visited
	private Node checkNodeForEnemyFort(Node x) {
		Node nodeToReturn = null;
		if (x != null) {
			if(x.getFortstate().equals(FortState.EnemyFortPresent))
				nodeToReturn = x;
			else {
				if(enemyHalf.contains(x)){
				visited.add(x);
				}
			}
		}
		return nodeToReturn;
	}
	
	//setting the neighbors for every single node.
	public void setNeighbors() {

		for (Node node : wholeMap) {
			getNeighbour(node);
		}
	}

	//getting the current node from the map
	public Node getCurrentNode() {
		for (Node node : wholeMap)
			if (node.getPlayerPositionState().equals(PlayerPositionState.MyPosition)
					|| node.getPlayerPositionState().equals(PlayerPositionState.BothPlayerPosition)) {
				return node;
			}
		return null;
	}

	// actually setting the neighbors from the node after checking if its a legal node
	private Map<Node, Integer> getNeighbour(Node node) {
		Map<Node, Integer> neighbors = new HashMap<>();
		if (!node.getType().equals(NodeType.WATER)) {

			Node x = legalNode(node.getX() - 1, node.getY());
			addDestinationForNode(node, x);

			x = legalNode(node.getX() + 1, node.getY());
			addDestinationForNode(node, x);

			x = legalNode(node.getX(), node.getY() - 1);
			addDestinationForNode(node, x);

			x = legalNode(node.getX(), node.getY() + 1);
			addDestinationForNode(node, x);
		}
		return neighbors;
	}

	//Adding a connection between neighbors and calculating their distance and setting it.
	private void addDestinationForNode(Node node, Node x) {
		if (x != null) {
			int distance = calculateDistance(node, x);
			node.addDestination(x, distance);
		}
	}

	//Finding my half which should be used to find my treasure.
	private void findMyHalf() {
		Node castle;
		boolean castlePositionNormal = false;
		for (Node node : wholeMap)
			if (node.getPlayerPositionState().equals(PlayerPositionState.MyPosition)) {
				castle = node;
				if (castle.getX() < 8 && castle.getY() < 4) {
					castlePositionNormal = true;
				}
			}

		for (Node node : wholeMap) {
			if (node.getX() < 8 && node.getY() < 4 && castlePositionNormal)
				myHalfList.add(node);
			else if ((node.getX() > 7 || node.getY() > 3) && !castlePositionNormal)
				myHalfList.add(node);
		}
	}
	
	//Setting the enemyHalfMap which is used in the search of the enemy Fort.
	private void setEnemyHalfList() {
		for (Node n : wholeMap)
			if (!myHalfList.contains(n))
				enemyHalf.add(n);
	}

	// Calculating the distance from node to x and returning the cost 
	//The cost is also the number of repeats sending of the same movement way between the two nodes
	private static int calculateDistance(Node node, Node x) {
		if (node.getType().equals(NodeType.GRASS) && x.getType().equals(NodeType.GRASS))
			return 2;
		else if (node.getType().equals(NodeType.MOUNTAIN) && x.getType().equals(NodeType.MOUNTAIN))
			return 4;
		else if (node.getType().equals(NodeType.GRASS) && x.getType().equals(NodeType.MOUNTAIN)
				|| node.getType().equals(NodeType.MOUNTAIN) && x.getType().equals(NodeType.GRASS))
			return 3;
		return 0;
	}

	// Checking if the node is legal (not water)
	private Node legalNode(int x, int y) {
		for (Node node : wholeMap) {
			if (node.getX() == x && node.getY() == y && !node.getType().equals(NodeType.WATER)) {
				return node;
			}
		}
		return null;
	}

	// checking on which side the neighbor node is and returning the MovementWay and
	// the cost which will be returned in the calculateNextMove class
	private Map<Direction, Integer> checkSideOdAdijacent(Node current, Node neighbor) {
		Map<Direction, Integer> mapOfWayAndNo = new HashMap<>();
		if (current.getX() + 1 == neighbor.getX() && current.getY() == neighbor.getY())
			mapOfWayAndNo.put(Direction.RIGHT, calculateDistance(current, neighbor));

		if (current.getX() - 1 == neighbor.getX() && current.getY() == neighbor.getY())
			mapOfWayAndNo.put(Direction.LEFT, calculateDistance(current, neighbor));

		if (current.getX() == neighbor.getX() && current.getY() + 1 == neighbor.getY())
			mapOfWayAndNo.put(Direction.DOWN, calculateDistance(current, neighbor));

		if (current.getX() == neighbor.getX() && current.getY() - 1 == neighbor.getY())
			mapOfWayAndNo.put(Direction.UP, calculateDistance(current, neighbor));

		return mapOfWayAndNo;
	}

	public void setMapToCheck(List<Node> mapToCheck) {
		this.wholeMap.clear();
		this.wholeMap.addAll(mapToCheck);
	}

	public void setCurrent(Node current) {
		this.current = current;
	}

	public Node getCurrent() {
		return this.current;
	}

	public List<Node> getObisao() {
		return listOfShortestPath;
	}

	public void setObisao(List<Node> obisao) {
		this.listOfShortestPath.addAll(obisao);
	}

}
