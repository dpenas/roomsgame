package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import net.slashie.util.Pair;
import util.RandUtil;

public class GrammarsOperational {
	
	public static Pair<Integer, JsonObject> selectGrammar(JsonObject object, ArrayList<Float> probabilities) {
		Pair<Integer, JsonObject> result = new Pair<Integer, JsonObject>(0, null);
//		int randNumber = RandUtil.RandomNumber(0, 99);
//		System.out.println("randNumber: " + randNumber);
//		float accumulatedProbability = 0;
//		int position = 0;
//		for (float probability: probabilities) {
//			System.out.println("Probabilities: " + probability);
//			accumulatedProbability += probability;
//			if (randNumber > accumulatedProbability) {
//				position++;
//			}
//		}
//		System.out.println("SelectedPosition: " + position);
//		JsonObject grammar = GrammarsRetrieval.getGrammar(object, position);
//		result.setA(position);
//		result.setB(grammar);
//		return result;
		
		System.out.println("EntrySet size: " + object.entrySet().size());
		System.out.println("Object: " + object);
		
		int position = RandUtil.RandomNumber(0, object.entrySet().size());
		JsonObject grammar = GrammarsRetrieval.getGrammar(object, position);
		result.setA(position);
		result.setB(grammar);
		return result;
		
	}
	
	private static boolean isArray(String string) {
		if (string.equals("array")) {
			return true;
		}
		return false;
	}
	
	public static ArrayList<Float> recalculateProbabilities(ArrayList<Float> probabilities, int selectedPosition) {
		System.out.println("RecalculateProbabilities: ");
		System.out.println("Size: " + probabilities.size());
		float percentageSelectedChange = Math.round(100/probabilities.size());
		System.out.println("percentageSelectedChange: " + percentageSelectedChange);
		float percentageOtherChange = Math.round(Math.round(100/probabilities.size())/probabilities.size() - 1);
		System.out.println("percentageOtherChange: " + percentageOtherChange);
		ArrayList<Float> newProbabilities = new ArrayList<Float>();
		for (int i = 0; i < probabilities.size(); i++) {
			if (i != selectedPosition) {
				System.out.println("HERE!!!");
				float newProbability = probabilities.get(i) + percentageOtherChange;
				if (newProbability < 0) newProbability = 0;
				newProbabilities.add(newProbability);
			} else {
				System.out.println("HERE2!!!");
				float newProbability = probabilities.get(i) + percentageSelectedChange;
				if (newProbability < 0) newProbability = 0;
				newProbabilities.add(newProbability);
			}
		}
		System.out.println("New Probabilities!: " + newProbabilities.size());
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
