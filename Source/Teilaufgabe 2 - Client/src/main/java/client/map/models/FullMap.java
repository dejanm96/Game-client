package client.map.models;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Map class
 * @author Dejan Micic
 *
 */
public class FullMap {
	private List<Node> mapNodes;
	
	private Logger logger = LoggerFactory.getLogger(FullMap.class);
	
	public FullMap() { this.mapNodes = new ArrayList<>(); }
	
	public FullMap(List<Node> list) {
		logger.info("Map sucessfully generated!");
		this.mapNodes = list;
	}

	public List<Node> getMapNodes() {
		return mapNodes;
	}

	public void setMapNodes(List<Node> mapNodes) {
		this.mapNodes.clear();
		this.mapNodes.addAll(mapNodes);
	}
	
	
}
