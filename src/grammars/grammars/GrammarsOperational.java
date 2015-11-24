package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import util.RandUtil;

public class GrammarsOperational {
	
	public static JsonObject selectGrammar(JsonObject object, ArrayList<Integer> probabilities) {
		int randNumber = RandUtil.RandomNumber(0, 99);
		float accumulatedProbability = 0;
		int position = 0;
		for (int probability: probabilities) {
			accumulatedProbability += probability;
			if (randNumber <= accumulatedProbability) {
				return GrammarsRetrieval.getGrammar(object, position);
			}
			position++;
		}
		return null;
	}
	
	private static boolean isArray(String string) {
		if (string.equals("array")) {
			return true;
		}
		return false;
	}
}
