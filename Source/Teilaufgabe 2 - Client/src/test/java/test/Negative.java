package test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.function.Executable;

import client.exceptions.MapNotValidException;
import client.helpers.MapValidator;
import client.map.enums.NodeType;
import client.map.generator.MapGenerator;
import client.map.models.Node;

public class Negative {

	 @Test
	 public void testingThrow() {
		 MapGenerator generator = new MapGenerator();
		 Executable validationTest = () -> {
			List<Node> halfMap = generator.newList();
			
			for(Node n : halfMap) {
				if(n.getType().equals(NodeType.WATER))
					n.setType(NodeType.GRASS);	
			}
			
		 MapValidator validator = new MapValidator(halfMap);
		 
		 validator.validation();
			
		 };
	 
		 assertThrows(MapNotValidException.class, validationTest, "Not generated sucesfully");
	 }
}
