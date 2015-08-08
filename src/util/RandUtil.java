package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	
	public static Tuple<Integer, Integer> inputMoveInterpretation(int input, List<Integer> movementInput, ActiveCharacter character){
		int down = movementInput.get(0);
		int up = movementInput.get(1);
		int right = movementInput.get(2);
		int left= movementInput.get(3);
		
		if (input == left) return new Tuple<Integer, Integer>(character.getPosition().x - 1, character.getPosition().y);
		if (input == right) return new Tuple<Integer, Integer>(character.getPosition().x + 1, character.getPosition().y);
		if (input == down) return new Tuple<Integer, Integer>(character.getPosition().x, character.getPosition().y - 1);
		if (input == up) return new Tuple<Integer, Integer>(character.getPosition().x, character.getPosition().y + 1);
		return null;
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
