package test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.List;

import org.junit.Test;

import client.map.enums.NodeType;
import client.map.generator.MapGenerator;
import client.map.models.Node;

import static org.hamcrest.Matchers.equalTo;


public class Testclasses {

	@Test
	public void checkMapHalf_makingHalfMap_size32() {
	
		MapGenerator generator = new MapGenerator();
		
		List<Node> newList = generator.newList();

		int result = newList.size();
		
		assertThat(result, is(equalTo(32)));
		
	}
	
	@Test
	public void checkMapHalf_makingHalfMap_numberOfWater() {
	
		MapGenerator generator = new MapGenerator();
		
		List<Node> newList = generator.newList();
		
		int numberOfWater = 0;

		for(Node node : newList)
			if(node.getType().equals(NodeType.WATER))
				numberOfWater++;
		
		assertThat(numberOfWater, is(equalTo(5)));
		
	}
	
	@Test
	public void checkMapHalf_makingHalfMap_numberOfGrass() {
	
		MapGenerator generator = new MapGenerator();
		
		List<Node> newList = generator.newList();
		int numberOfGrass = 0;
		
		for(Node node : newList)
			if(node.getType().equals(NodeType.GRASS))
				numberOfGrass++;
		
		assertThat(numberOfGrass, is(equalTo(22)));
		
	}
	
	
	@Test
	public void checkMapHalf_makingHalfMap_numberOfMountain() {
	
		MapGenerator generator = new MapGenerator();
		
		List<Node> newList = generator.newList();

		int numberOfMnt = 0;
		
		for(Node node : newList)
			if(node.getType().equals(NodeType.WATER))
				numberOfMnt++;
		
		assertThat(numberOfMnt, is(equalTo(5)));
		
	}
	
	@Test
	public void checkMapHalf_makingHalfMap_settingTypeOfNode() {
	
		Node node = new Node(0, 0);
	
		node.setType(NodeType.WATER);
		
		assertThat(node.getType(), is(equalTo(NodeType.WATER)));
		
	}
	
	
	@Test
	public void checkMapHalf_makingHalfMap_fortSet() {
	
		MapGenerator generator = new MapGenerator();
		
		List<Node> newList = generator.newList();
		
		boolean fortPresent = false; 
		
		for(Node node : newList)
			if(node.isFortPresent())
				fortPresent = true;
		
		assertThat(fortPresent, is(equalTo(true)));
		
	}
	
}
