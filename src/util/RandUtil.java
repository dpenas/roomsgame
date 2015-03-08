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
	
	public static Tuple<Integer, Integer> inputMoveInterpretation(int input, ActiveCharacter character){
		
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
	
	public static int separationTuples(Tuple<Integer, Integer> tuple1, Tuple<Integer, Integer> tuple2){
		return Math.abs(tuple1.x - tuple2.x) + Math.abs(tuple1.y - tuple2.y);
	}
	
	public static boolean containsString(char[] array, char givenChar){
		for (char stringArray: array){
			if (stringArray == givenChar){
				return true;
			}
		}
		return false;
	}
	
	public static boolean sameTuple(Tuple<Integer, Integer> tuple1, Tuple<Integer, Integer> tuple2){
		if (tuple1.x == tuple2.x && tuple1.y == tuple2.y){
			return true;
		}
		return false;
	}
}
