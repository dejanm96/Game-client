package server.validation;

import java.util.ArrayList;
import java.util.List;

public class RulesList {

	
	private static final IRule CheckMapSize = new CheckMapSize();
	private static final IRule CheckNodeTypeCount = new CheckNodeTypeCount();
	private static final IRule FortSetGood = new FortSetGood();
	private static final IRule IslandCheck = new IslandCheck();
	private static final IRule WaterOnEdgesCheck = new WaterOnEdgesCheck();
	private static final IRule checkMapCoordinates = new CheckMapCoordinates();
	private static final IRule EnoughPlayerRegistered = new EnoughPlayerRegistered();
	
	List<IRule> allRules = new ArrayList<>();
	
	public RulesList() {
		addAllRules();
	}

	private void addAllRules() {
		allRules.add(CheckMapSize);
		allRules.add(CheckNodeTypeCount);
		allRules.add(FortSetGood);
		allRules.add(IslandCheck);
		allRules.add(WaterOnEdgesCheck);
		allRules.add(checkMapCoordinates);
		allRules.add(EnoughPlayerRegistered);
	}

	public List<IRule> getAllRules() {
		return allRules;
	}

	public void setAllRules(List<IRule> allRules) {
		this.allRules = allRules;
	}
	
}
