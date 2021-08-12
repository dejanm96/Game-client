package server.game;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.PlayerState;
import server.converter.ConvertLocalEnumsToServerEnums;
import server.converter.MapConverter;
import server.map.Node;
import server.validation.RulesList;
import server.wholemap.CombineMapHalfs;

public class Game {
	
	private List<Player> playerList = new ArrayList<>();
	private String uniqueGameIdentifier;
	private Timestamp gameBeginTime;
	private String gameStateID;
	private ConvertLocalEnumsToServerEnums converter;
	private List<Node> wholeMap;
	private MapConverter mapConverter = new MapConverter();
	
	private RulesList rulesList;
	
	public Game(String uniqueGameID) {
		uniqueGameIdentifier = uniqueGameID;
		setGameBeginTime(new Timestamp(System.currentTimeMillis()));
		setRulesList(new RulesList());
		converter = new ConvertLocalEnumsToServerEnums();
	}

	public Game() {	  }

	
	public Collection<PlayerState> getStates(String gameID, String uniquePlayerID) {
		Collection<PlayerState> result = new ArrayList<PlayerState>();
				
		for(Player player : playerList)
			if(player.getUniquePlayerID().equals(uniquePlayerID))
				result.add(converter.convertPlayerToState(player));
			else
				result.add(converter.convertToFakeState(player));
		
		return result;
	}
	
	public void createWholeMap() {
		CombineMapHalfs combineMapHalfs = new CombineMapHalfs(playerList);
		setWholeMap(combineMapHalfs.randomCombineMapHalfs());
	}

	public List<Node> getCopyOfMap(){
		List<Node> copy = new ArrayList<>();
		
		for(Node node : wholeMap) {
			copy.add(new Node(node.getType(), node.getFortstate(), node.getPlayerPositionState(), node.getTreasureState(), node.isFortPresent(), node.getX(), node.getY()));

		}
		
		return copy;
	}
	
	public List<Player> getPlayers() {
		return playerList;
	}

	public void addPlayer(Player player) {
		this.playerList.add(player);
	}

	public String getUniqueGameIdentifier() {
		return uniqueGameIdentifier;
	}
	
	
	public void setUniqueGameIdentifier(String uniqueGameIdentifier) {
		this.uniqueGameIdentifier = uniqueGameIdentifier;
	}


	public Timestamp getGameBeginTime() {
		return gameBeginTime;
	}

	public void setGameBeginTime(Timestamp timestamp) {
		this.gameBeginTime = timestamp;
	}

	public RulesList getRulesList() {
		return rulesList;
	}

	public void setRulesList(RulesList rulesList) {
		this.rulesList = rulesList;
	}

	public String getGameStateID() {
		return gameStateID;
	}

	public void setGameStateID(String gameStateID) {
		this.gameStateID = gameStateID;
	}

	public boolean checkIfBothMapsAreSent() {
		boolean bothMapSent = false;
		
		Player player1 = playerList.get(0);
		Player player2 = playerList.get(1);

		if(player1.isMapAlreadySent() && player2.isMapAlreadySent())
			bothMapSent = true;
		
		return bothMapSent;
	}

	public FullMap getWholeMap(String playerID) {
		Collection<FullMapNode> fullMapList = null;;
		if(wholeMap != null)
			fullMapList = mapConverter.convertClientToServerMap(getCopyOfMap(), getPlayers(), playerID);
		return new FullMap(fullMapList);
	}

	public List<Node> getMapList(){
		return this.wholeMap;
	}
	
	public void setWholeMap(List<Node> wholeMap) {
		this.wholeMap = wholeMap;
	}

	
	
}
