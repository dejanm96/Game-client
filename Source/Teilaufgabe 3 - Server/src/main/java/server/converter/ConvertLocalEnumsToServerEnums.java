package server.converter;

import java.util.UUID;

import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.ETreasureState;
import MessagesGameState.PlayerState;
import server.enums.FortState;
import server.enums.PlayerPositionState;
import server.enums.TreasureState;
import server.game.Player;

public class ConvertLocalEnumsToServerEnums {

	private MapConverter mapConverter = new MapConverter();;
	
	public ConvertLocalEnumsToServerEnums() {  
		
		}
	
	public PlayerState convertPlayerToState(Player player) {
		
		return new PlayerState(player.getPlayerFirstName(), player.getPlayerLastName(), player.getPlayerStudentID(), 
				mapConverter.convertLocalPlayerGameStateToServer(player.getGameStateValue()), new UniquePlayerIdentifier(player.getUniquePlayerID()), player.getTreasureCollected());
	}

	public PlayerState convertToFakeState(Player player) {
		return new PlayerState(player.getPlayerFirstName(), player.getPlayerLastName(), player.getPlayerStudentID(), 
				mapConverter.convertLocalPlayerGameStateToServer(player.getGameStateValue()), new UniquePlayerIdentifier(UUID.randomUUID().toString()), player.getTreasureCollected());
	}
	
	public EPlayerPositionState convertPlayerPositionState(PlayerPositionState position) {
		EPlayerPositionState result = null; 
		
		switch (position) {
		case BothPlayerPosition:
			result = EPlayerPositionState.BothPlayerPosition;
			break;
		case EnemyPlayerPosition:
			result = EPlayerPositionState.EnemyPlayerPosition;
			break;
		case MyPosition:
			result = EPlayerPositionState.MyPosition;
			break;
		case NoPlayerPresent:
			result = EPlayerPositionState.NoPlayerPresent;
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public ETreasureState convertTreasureState(TreasureState treasure) {
		ETreasureState result = null; 
		
		switch (treasure) {
		case MyTreasureIsPresent:
			result = ETreasureState.MyTreasureIsPresent;
			break;
		case NoOrUnknownTreasureState:
			result = ETreasureState.NoOrUnknownTreasureState;
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public EFortState convertFortState(FortState fortState) {
		EFortState result = null; 
		
		switch (fortState) {
		case EnemyFortPresent:
			result = EFortState.EnemyFortPresent;
			break;
		case MyFortPresent:
			result = EFortState.MyFortPresent;
			break;
		case NoOrUnknownFortState:
			result = EFortState.NoOrUnknownFortState;
			break;
		default:
			break;
		}
		
		return result;
	}
}
