package server.database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.PlayerRegistration;
import server.enums.PlayerGameState;
import server.exceptions.ServerException;
import server.game.Game;
import server.game.Player;
import server.gamestate.LocalGameState;

public class GameDatabase {

	private static GameDatabase gameObject = null; 
	private List<Game> gameList = new ArrayList<>();
	private Logger logger = LoggerFactory.getLogger(GameDatabase.class);
	private LocalGameState localGameState = new LocalGameState();
	
	private GameDatabase() { }
	
	
	 public static GameDatabase singleObject() {
			
			if(gameObject == null) {
				gameObject = new GameDatabase();
			}
			return gameObject;
		}
	
	public void addNewGame(String gameIdentifier) {
		gameList.add(new Game(gameIdentifier));
		logger.info("Game with the uniqueGameID: " + gameIdentifier + " has been added to the Database");
	}

	
	public List<Game> getAllGames(){
		return gameList;
	}
	
	public synchronized Game getGameByID(String gameIdentifier) {

		Game gameByID = null;
		for (Game game : gameList)
			if (game.getUniqueGameIdentifier().equals(gameIdentifier))
				gameByID = game;

		return gameByID;
	}
	
	public void registerPlayerToGame(String gameID, String uniquePlayerID, PlayerRegistration registration) {
		
		Game game = getGameByID(gameID);
		game.setGameStateID(UUID.randomUUID().toString());
			
			if(game.getPlayers().size() <= 2) {
				Player newPlayer = new Player();
				newPlayer.setPlayerFirstName(registration.getStudentFirstName());
				newPlayer.setPlayerLastName(registration.getStudentLastName());
				newPlayer.setPlayerStudentID(registration.getStudentID());
				newPlayer.setUniquePlayerID(uniquePlayerID);
				if(game.getPlayers().size() < 2) {
					newPlayer.setGameStateValue(PlayerGameState.ShouldWait);
				}else
					localGameState.setPlayerGameStatesAtStart(getGameByID(gameID).getPlayers(), getPlayerByUniquePlayerID(gameID, uniquePlayerID));
				
				game.addPlayer(newPlayer);
				if(game.getPlayers().size() == 2) {
					localGameState.setPlayerGameStatesAtStart(getGameByID(gameID).getPlayers(), newPlayer);
					
				}
				logger.info("Player with the uniquePlayerID: " + uniquePlayerID + " has been registered to the game " + gameID);
		
		}else {
			logger.error("Already 2 players registered!");
			throw new ServerException("Game registration!", "Already 2 games registered");
		}
	}


	public synchronized Player getPlayerByUniquePlayerID(String uniqueGameId, String uniquePlayerID) {
		List<Player> registeredPlayerList = new ArrayList<>();
		boolean gameFound = false;
		
		Player player = new Player();
		Player foundPlayer = null;
		
		player.setUniquePlayerID(uniquePlayerID);
		
		for(Game game : gameList) {
			if(game.getUniqueGameIdentifier().equals(uniqueGameId)) {
				registeredPlayerList = game.getPlayers();
				gameFound = true;
			}
		}
		
		if(gameFound) {
			for(Player players : registeredPlayerList)
				if(player.getUniquePlayerID().equals(players.getUniquePlayerID()))
					foundPlayer = players;
		}
		return foundPlayer;
	}
	
	public void deleteGameOlderThenTenMinutes() {
		ScheduledExecutorService scheduledExecutorService =
		        Executors.newScheduledThreadPool(1);
		
		int tenMinutes = 600000;
		
		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				if(gameList.size() > 0)
				for(Game game : gameList) {
					if(System.currentTimeMillis() - game.getGameBeginTime().getTime() >= tenMinutes) {
						gameList.remove(game);
						logger.info("Game with UniqueID: " + game.getUniqueGameIdentifier() + " was deleted because it was older then 10minutes!");
						run();
					}
				}
			}
		}, 0, 2, TimeUnit.SECONDS);
		
	}


	public void deleteOldest() {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Game oldest = null;
		
		long difference = 0;
		
		for(Game game : gameList)
			if(currentTime.getTime() - game.getGameBeginTime().getTime() > difference) {
				difference = currentTime.getTime() - game.getGameBeginTime().getTime();
				oldest = game;
			}
		gameList.remove(oldest);
		logger.info("Deleting oldest game with the id: " + oldest.getUniqueGameIdentifier());

	}
}
