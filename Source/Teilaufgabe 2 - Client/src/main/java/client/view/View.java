package client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import client.helpers.MapPrinter;
import client.map.enums.PlayerGameState;
import client.map.models.FullMap;
import client.model.GameStateModel;

/**
 * The View class is used to write out the map. Every time that the 
 * property of the map is changed, property change will fire which
 * leads to firing the view function.
 * @author Dejan Micic
 *
 */
public class View implements PropertyChangeListener {
	
	MapPrinter printer = new MapPrinter();
	
	public View(GameStateModel model) {
		model.addListener(this);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = (String) evt.getPropertyName();
		if(eventName.equals("Treasure collected")) { 
			System.out.println("################################");
			System.out.println("#Treasure has been collected!!!#");
			System.out.println("################################");
		}
		else if(eventName.equals("State")) {
		
			Object o = evt.getNewValue();
			PlayerGameState myGameState = null;
						
			if(o instanceof PlayerGameState) {
				myGameState = (PlayerGameState) o;
			}
			if(myGameState.equals(PlayerGameState.Won)) {
				System.out.println("********************************");
				System.out.println("************* " + myGameState + " **************");
				System.out.println("********************************");
			}
			else if(myGameState.equals(PlayerGameState.Lost)) {
				System.out.println("################################");
				System.out.println("############# " + myGameState + " #############");
				System.out.println("################################");
			}
		} else{	
		String name = (String) evt.getPropertyName();
		System.out.println(name);
		
		Object o = evt.getNewValue();
		FullMap map = new FullMap();
		if(o instanceof FullMap) {
			map = (FullMap) o;
		}
		
 		printer.printMap(map.getMapNodes());
		}
	}
}
