package util;

import java.util.ArrayList;
import java.util.Random;

import characters.active.ActiveCharacter;

public class RandUtil {

	/**
	* It returns a number between two given values
	* @param interval
	* @return
	*/
	
	public static int RandomNumber(int minValue, int maxValue){
		Random randomGenerator = new Random();
		int randomIntValue = randomGenerator.nextInt(maxValue - minValue) + minValue;
		return randomIntValue;
	}
	
	public static Tuple<Integer, Integer> inputInterpretation(int input, ActiveCharacter character){
		
		switch (input){
		case 0:
			return new Tuple<Integer, Integer>(character.getPosition().x - 1, character.getPosition().y);
		case 1:
			return new Tuple<Integer, Integer>(character.getPosition().x + 1, character.getPosition().y);
		case 2:
			return new Tuple<Integer, Integer>(character.getPosition().x, character.getPosition().y - 1);
		case 3:
			return new Tuple<Integer, Integer>(character.getPosition().x, character.getPosition().y + 1);
		default:
			return null;
		}
	}
	
	public static boolean containsTuple(Tuple<Integer, Integer> tuple, ArrayList<Tuple<Integer, Integer>> array){
		for (Tuple<Integer, Integer> tuple2 : array){
			if (tuple.x == tuple2.x && tuple.y == tuple2.y) return true;
		}
		return false;
	}
}
