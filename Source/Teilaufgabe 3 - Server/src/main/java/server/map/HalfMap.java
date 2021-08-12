package server.map;

import java.util.ArrayList;
import java.util.List;


public class HalfMap {
	
	private List<Node> mapNodes;
	
	public HalfMap() { 
		this.mapNodes = new ArrayList<>(); 
	}
	
	
	public HalfMap(List<Node> list) {
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
