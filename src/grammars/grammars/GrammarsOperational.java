package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import util.RandUtil;

public class GrammarsOperational {
	
	public static JsonObject selectGrammar(JsonObject object, ArrayList<Float> probabilities) {
		int randNumber = RandUtil.RandomNumber(0, 99);
		float accumulatedProbability = 0;
		int position = 0;
		for (float probability: probabilities) {
			accumulatedProbability += probability;
			if (randNumber > accumulatedProbability) {
				position++;
			}
		}
		return GrammarsRetrieval.getGrammar(object, position);
	}
	
	private static boolean isArray(String string) {
		if (string.equals("array")) {
			return true;
		}
		return false;
	}
	
	private static ArrayList<Float> recalculateProbabilities(ArrayList<Float> probabilities, int selectedPosition) {
		float percentageSelectedChange = Math.round(100/probabilities.size());
		float percentageOtherChange = Math.round(Math.round(100/probabilities.size())/probabilities.size() - 1);
		ArrayList<Float> newProbabilities = new ArrayList<Float>();
		for (int i = 0; i < probabilities.size(); i++) {
			if (i != selectedPosition) {
				float newProbability = probabilities.get(i) + percentageOtherChange;
				if (newProbability < 0) newProbability = 0;
				newProbabilities.add(newProbability);
			} else {
				float newProbability = probabilities.get(i) + percentageSelectedChange;
				if (newProbability < 0) newProbability = 0;
				newProbabilities.add(newProbability);
			}
		}
		return newProbabilities;
	}
	
	public static ArrayList<Float> initializeProbabilities(int numberProbabilities) {
		float probability = (float) Math.round(100/numberProbabilities);
		ArrayList<Float> probabilities = new ArrayList<Float>();
		for (int i = 0; i < numberProbabilities; i++) {
			probabilities.add(probability);
		}
		return probabilities;
	}
}
