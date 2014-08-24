package util;

import java.util.Random;

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
}
