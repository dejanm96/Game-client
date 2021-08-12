package server.gamestate;

import java.util.List;
import java.util.Random;

import server.enums.PlayerGameState;
import server.game.Player;

public class LocalGameState {

	private PlayerGameState playerGameState;
	private String gameStateID;

	
	public LocalGameState() { }

	public PlayerGameState getPlayerGameState() {
		return playerGameState;
	}

	public void setPlayerGameState(PlayerGameState playerGameState) {
		this.playerGameState = playerGameState;
	}

	public String getGameStateID() {
		return gameStateID;
	}

	public void setGameStateID(String gameStateID) {
		this.gameStateID = gameStateID;
	}
	
	public void setPlayerGameStatesAtStart(List<Player> playerList, Player player) {
		Random random = new Random();
		int randomNumber = random.nextInt();

		for (Player allPlayers : playerList) {

			if (randomNumber % 2 == 0) {
				if (allPlayers.getUniquePlayerID().equals(player.getUniquePlayerID())) {
					allPlayers.setGameStateValue(PlayerGameState.ShouldActNext);
				}
				else {
					allPlayers.setGameStateValue(PlayerGameState.ShouldWait);
				}
			} else {
				if (allPlayers.getUniquePlayerID().equals(player.getUniquePlayerID())) {
					allPlayers.setGameStateValue(PlayerGameState.ShouldWait);
				}
				else {
					allPlayers.setGameStateValue(PlayerGameState.ShouldActNext);
				}
			}
		}
	}
	
	public void setStateForPlayers(List<Player> list, Player currentPlayer) {
		for(Player player : list) 
			if(player.equals(currentPlayer)) {
				player.setGameStateValue(PlayerGameState.ShouldWait);
			}
			else {
				player.setGameStateValue(PlayerGameState.ShouldActNext);
			}
		}	
	
	public void setLostOrWon(List<Player> list, String gameID, String uniquePlayerID) {
		for(Player players : list)
			if(players.getUniquePlayerID().equals(uniquePlayerID)) {
				players.setGameStateValue(PlayerGameState.Lost);
			}
			else {
				players.setGameStateValue(PlayerGameState.Won);
			}
	}
}
