package server.validation;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;

public interface IRule {
	
	public void newGame();
	
	public void registerPlayer(String gameID, PlayerRegistration playerRegistration);
	
	public void sendHalfMap(String gameID, HalfMap halfMap);
	
	public void gameState(String gameID, String playerID);
}
