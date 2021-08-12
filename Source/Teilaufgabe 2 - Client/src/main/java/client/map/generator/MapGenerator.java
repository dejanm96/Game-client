package client.map.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.helpers.MapValidator;
import client.map.enums.NodeType;
import client.map.models.Node;

/**
 * Class for generating the map 
 * @author Dejan Micic
 *
 */
public class MapGenerator {

	private Logger logger = LoggerFactory.getLogger(MapGenerator.class);
	
	private List<Node> nodeList = new ArrayList<>();
		
	public List<Node> newList() {
		nodeList = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 8; j++) {
				nodeList.add(new Node(j, i));
			}
		}
		setTypeOfNode();
		
		return nodeList;
	}
	
	/**
	 * Setting the type of the nodes. First randomly placing 5 Water fields, 
	 * then 5 Mountains and at last i just fill the rest of the fields with 
	 * grass fields
	 */
	private void setTypeOfNode() {
		int numberOfWaterFields = 0, numberOfMountainFields = 0;

		while(numberOfWaterFields <= 4) {
			int randomNumber = new Random().nextInt(nodeList.size());
			if(nodeList.get(randomNumber).getType() == null) {
				nodeList.get(randomNumber).setType(NodeType.WATER);
				numberOfWaterFields++;
			}
		}

		while(numberOfMountainFields <= 4) {
			int randomNumber = new Random().nextInt(nodeList.size());
			if(nodeList.get(randomNumber).getType() == null) {
				nodeList.get(randomNumber).setType(NodeType.MOUNTAIN);
				numberOfMountainFields++;
			}
		}
		
		for(Node n : nodeList) 
			if(n.getType() == null) 
				n.setType(NodeType.GRASS);
		
		//setting the fort on a random grass field
		boolean fortSet = false;
		while(!fortSet) {
			int randomNo = new Random().nextInt(nodeList.size());
			if(nodeList.get(randomNo).getType().equals(NodeType.GRASS)) {
				nodeList.get(randomNo).setFortPresent(true);
				fortSet = true;
			}
		}
		
		
		//Constructor for the MapChecking
		MapValidator mapChecking = new MapValidator(nodeList);

		
		//if i have more water on the sides then I can have or if I have an island i make i new map.
		if(!mapChecking.validation()) {
			logger.info("Map not generated correctly! Generating new...");
			nodeList.clear();
			newList();
		}
		
		
	}
	
	public List<Node> getNodeList() {
		return nodeList;
	}
}
