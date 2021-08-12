package server.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGameIDCreator {

	private static final String alphabetUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String numbers = "0123456789";
	private static final String theWholeAlphabet = alphabetUpperCase + alphabetUpperCase.toLowerCase() + numbers;
	
	private List<String> generatedGameIDs = new ArrayList<>();
	
 	public RandomGameIDCreator() { }
 
 	public String getRandomGameID() {
 		
 		char[] temporaryGameID = new char[5];
 		Random randomNumber = new Random();
 		int nextRandomNumber = 0;
 		
 		for(int i = 0; i < 5; i++) {
 			nextRandomNumber = randomNumber.nextInt(theWholeAlphabet.length());
 			temporaryGameID[i] = theWholeAlphabet.charAt(nextRandomNumber);
 		}
 		
 		String uniqueGameID = new String(temporaryGameID);
 		//Brisi gameid posle 10min
 		if(generatedGameIDs.contains(uniqueGameID))
 			getRandomGameID();
 		else
 			generatedGameIDs.add(uniqueGameID);
 		
 		return uniqueGameID;
 	}
 	
}
