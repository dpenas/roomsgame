package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import items.Item;
import net.slashie.util.Pair;
import util.RandUtil;

public abstract class GrammarSelector {
	private static String[] WORD_IMPORTANCE = {"V", "N", "ADJ", "DET"};
	private GrammarIndividual grammar;
	private JsonObject wordsGrammar;
	
	public GrammarSelector(GrammarIndividual grammar, JsonObject wordsGrammar) {
		this.grammar = grammar;
		this.wordsGrammar = wordsGrammar;
	}
	
	protected abstract ArrayList<Pair<String, JsonArray>> fillWords();
	protected abstract ArrayList<Pair<String, JsonArray>> changeValue(ArrayList<Pair<String, JsonArray>> sentenceArray, String valueToChange, String changeToValue, String typeChangeToValue);
	
	protected String getImportantRestriction(String value1Type, String value1, String value2Type, String value2) {
		for (int i = 0; i < WORD_IMPORTANCE.length; i++) {
			if (WORD_IMPORTANCE[i].equals(value1Type)) {
				return value2;
			}
			if (WORD_IMPORTANCE[i].equals(value2Type)) {
				return value1;
			}
		}
		return "";
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyNumRestrictions(Pair<String, String> restriction, ArrayList<Pair<String, JsonArray>> sentenceArray) {
//		for (Pair<String, JsonArray> sentence : sentenceArray) {
//			System.out.println(sentence.getB());
//		}
		ArrayList<String> grammar = this.getGrammar().getGrammar().get("keys");
		String firstType = restriction.getA();
		String secondType = restriction.getB();
		JsonArray restrictions1 = sentenceArray.get(grammar.indexOf(firstType)).getB();
		JsonArray restrictions2 = sentenceArray.get(grammar.indexOf(secondType)).getB();
//		System.out.println("restrictions1: " + restrictions1);
//		System.out.println("restrictions2: " + restrictions2);
		String value1 = JSONParsing.getElement(restrictions1, "translation");
		String value2 = JSONParsing.getElement(restrictions2, "translation");
		String value1Num = JSONParsing.getElement(restrictions1, "num");
		String value2Num = JSONParsing.getElement(restrictions2, "num");
//		System.out.println("Value1Num: " + value1Num);
//		System.out.println("Value2Num: " + value2Num);
//		System.out.println("Value1: " + value1);
//		System.out.println("Value2: " + value2);
		if (!value1Num.equals(value2Num) && value1Num.length() > 0 && value2Num.length() > 0) {
			String changeToValue = "";
			String typeFirstRestriction = firstType.substring(0, firstType.indexOf("_"));
			String typeSecondRestriction = secondType.substring(0, secondType.indexOf("_"));
			String toChange = getImportantRestriction(typeFirstRestriction, value1, typeSecondRestriction, value2);
//			System.out.println("toChange: " + toChange);
			String typeChangeToValue = "";
			if (toChange.equals(value1)) {
//				System.out.println(restrictions1);
				changeToValue = JSONParsing.getElement(restrictions1, "numopposite");
				typeChangeToValue = typeFirstRestriction; 
				 
			} else {
				changeToValue = JSONParsing.getElement(restrictions2, "numopposite");
				typeChangeToValue = typeSecondRestriction;
			}
//			System.out.println("ChangeToValue: " + changeToValue);
//			System.out.println("TypeChangeToValue: " + typeChangeToValue);
			this.changeValue(sentenceArray, toChange, changeToValue, typeChangeToValue);
		}
		return sentenceArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyRestrictions(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		for(Pair<String, String> restriction : this.getGrammar().getRestrictions()) {
			int dotPointA = restriction.getA().indexOf(".");
			int dotPointB = restriction.getB().indexOf(".");
			String restrictionType = restriction.getA().substring(dotPointA + 1, restriction.getA().length());
			switch (restrictionType) {
				case "num": 
					String elementA = restriction.getA().substring(0, dotPointA);
					String elementB = restriction.getB().substring(0, dotPointB);
					Pair<String, String> pair = new Pair<String, String>(elementA, elementB);
					sentenceArray = applyNumRestrictions(pair, sentenceArray);
					break;
			}
		}
		return sentenceArray;
	}
	
	public abstract String getRandomSentence();
	
	public GrammarIndividual getGrammar() {
		return grammar;
	}

	public void setGrammar(GrammarIndividual grammar) {
		this.grammar = grammar;
	}
}
