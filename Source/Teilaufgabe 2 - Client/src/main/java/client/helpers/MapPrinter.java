package client.helpers;

import java.util.ArrayList;
import java.util.List;

import client.map.enums.FortState;
import client.map.enums.PlayerPositionState;
import client.map.enums.TreasureState;
import client.map.models.Node;

public class MapPrinter {
	private DataConverter dataConverter = new DataConverter();
	private List<Node> map = new ArrayList<>();
	
	
	public MapPrinter() { 	}
	
	//Here map is being printed
		public void printMap(List<Node> newList) {
			setMap(newList);
			
			if (!checkMapOrientation(newList)) {
				printer(8);
			} else {
				printer(16);
			}
		}

		private void printer(int size) {
			int counter = 0;
			for (Node n : map) {
				if (counter % size == 0)
					System.out.println("");
				if (n.getPlayerPositionState().equals(PlayerPositionState.MyPosition)
						|| n.getPlayerPositionState().equals(PlayerPositionState.BothPlayerPosition))
					System.out.print("[" + dataConverter.typeConverter(n.getType()) + " ++]");
				else if (n.getTreasureState().equals(TreasureState.MyTreasureIsPresent))
					System.out.print("[" + dataConverter.typeConverter(n.getType()) + " !!]");
				else if (n.getFortstate().equals(FortState.MyFortPresent))
					System.out.print("[" + dataConverter.typeConverter(n.getType()) + " ><]");
				else if (n.getFortstate().equals(FortState.EnemyFortPresent))
					System.out.print("[" + dataConverter.typeConverter(n.getType()) + " ??]");
				else if(n.getPlayerPositionState().equals(PlayerPositionState.EnemyPlayerPosition) 
						|| n.getPlayerPositionState().equals(PlayerPositionState.BothPlayerPosition))
					System.out.print("[" + dataConverter.typeConverter(n.getType()) + " --]");
				else
					System.out.print("[  " + dataConverter.typeConverter(n.getType()) + " ]");
				
				counter++;
			}
			System.out.println("");
			System.out.println("--------------------------------------------------------------------");
		}
		
		/**
		 * if the map is horizontal oriented this function will return true.
		 * 
		 * @return boolean horizontal
		 */
		public boolean checkMapOrientation(List<Node> newList) {
			boolean horizontal = false;
			for (Node n : newList)
				if (n.getX() >= 8) {
					horizontal = true;
					break;
				}
			return horizontal;
		}

		public List<Node> getMap() {
			return map;
		}

		public void setMap(List<Node> map) {
			this.map = map;
		}
		
		
}
