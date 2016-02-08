package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import net.slashie.util.Pair;

public abstract class GrammarSelector {
	private static String[] WORD_IMPORTANCE = {"SIMPLE", "SIMPLEPREP", "NP", "N", "DET", "GENERAL", "V", "ADJ", "ADJECTIVE"};
	private GrammarIndividual grammar;
	private JsonObject wordsGrammar;
	
	public GrammarSelector(GrammarIndividual grammar, JsonObject wordsGrammar) {
		this.grammar = grammar;
		this.wordsGrammar = wordsGrammar;
	}
	
	protected abstract ArrayList<Pair<String, JsonArray>> fillWords();
	protected abstract ArrayList<Pair<String, JsonArray>> changeValue(ArrayList<Pair<String, JsonArray>> sentenceArray, String valueToChange, String changeToValue, String typeChangeToValue);
	
	protected String getImportantRestriction(String value1Type, String value1, String value2Type, String value2) {
		System.out.println("I receive value1Type: " + value1Type + " value1: " + value1 + " value2Type: " 
				+ value2Type + " value2: " + value2);
		for (int i = 0; i < WORD_IMPORTANCE.length; i++) {
			if (WORD_IMPORTANCE[i].equals(value1Type)) {
				System.out.println("I'm returning value2");
				return value2;
			}
			if (WORD_IMPORTANCE[i].equals(value2Type)) {
				System.out.println("I'm returning value1");
				return value1;
			}
		}
		return "";
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyRestrictions(Pair<String, String> restriction, 
			ArrayList<Pair<String, JsonArray>> sentenceArray, ArrayList<Integer> numItems, String type) {
		
		ArrayList<String> grammar = this.getGrammar().getGrammar().get("keys");
		System.out.println("GRAMMAR: " + grammar);
		System.out.println(restriction.getA());
		System.out.println(restriction.getB());
		System.out.println("Yeah sentence array: ");
		for (int i = 0; i < sentenceArray.size(); i++) {
			if (sentenceArray.get(i) != null) {
				System.out.println(sentenceArray.get(i).getA());
				System.out.println(sentenceArray.get(i).getB());
			}
		}
		System.out.println("FIN Yeah sentence array: ");
		String firstType = restriction.getA();
		String secondType = restriction.getB();
		System.out.println("firstType: " + firstType);
		System.out.println("secondType: " + secondType);
		JsonArray restrictions1 = null;
		JsonArray restrictions2 = null;
		String restrictions1Value = "";
		String restrictions2Value = "";
		if (numItems == null) {
			restrictions1Value = sentenceArray.get(grammar.indexOf(firstType)).getA();
			restrictions2Value = sentenceArray.get(grammar.indexOf(secondType)).getA();
			restrictions1 = sentenceArray.get(grammar.indexOf(firstType)).getB();
			restrictions2 = sentenceArray.get(grammar.indexOf(secondType)).getB();
			System.out.println("restriction1 inside numitems: " + restrictions1);
		} else {
			int totalFirstItem = 0;
			int totalSecondItem = 0;
			for (int i = 0; i <= grammar.indexOf(firstType); i++) {
				totalFirstItem += numItems.get(i);
				System.out.println("TotalFirstItem: " + totalFirstItem);
				restrictions1 = sentenceArray.get(totalFirstItem - 1).getB();
				restrictions1Value = sentenceArray.get(totalFirstItem - 1).getA();
			}
			System.out.println("This is the problem: R1 " + restrictions1);
			for (Integer integer: numItems) {
				System.out.println("integer: " + integer);
			}
			for (int i = 0; i < grammar.indexOf(secondType); i++) {
				totalSecondItem += numItems.get(i);
				restrictions2 = sentenceArray.get(totalSecondItem).getB();
				restrictions2Value = sentenceArray.get(totalSecondItem).getA();
			}
			if (grammar.indexOf(secondType) == 0) {
				restrictions2 = sentenceArray.get(0).getB();
				restrictions2Value = sentenceArray.get(0).getA();
			}
		}
		System.out.println("restrictions1!: " + restrictions1Value);
		System.out.println("restrictions2!: " + restrictions2Value);
		String value1 = restrictions1Value;
		String value2 = restrictions2Value;
		String value1Num = JSONParsing.getElement(restrictions1, type);
		String value2Num = JSONParsing.getElement(restrictions2, type);
		System.out.println("Value1Num: " + value1Num);
		System.out.println("Value2Num: " + value2Num);
		System.out.println("Value1: " + value1);
		System.out.println("Value2: " + value2);
		if (value1Num != null && value2Num != null && !value1Num.equals(value2Num) 
				&& value1Num.length() > 0 && value2Num.length() > 0) {
			String changeToValue = "";
			String typeFirstRestriction = firstType.substring(0, firstType.indexOf("_"));
			String typeSecondRestriction = secondType.substring(0, secondType.indexOf("_"));
			System.out.println("Type first restriction: " + typeFirstRestriction);
			System.out.println("Type second restriction: " + typeSecondRestriction);
			String toChange = getImportantRestriction(typeFirstRestriction, value1, typeSecondRestriction, value2);
			System.out.println("toChange: " + toChange);
			String typeChangeToValue = "";
			if (toChange.equals(value1)) {
//				System.out.println(restrictions1);
				changeToValue = JSONParsing.getElement(restrictions1, type + "opposite");
				System.out.println("opposite thing: " + type+ "opposite");
				System.out.println("change to value shenanigangs: " + changeToValue);
				typeChangeToValue = typeFirstRestriction; 
				 
			} else {
				changeToValue = JSONParsing.getElement(restrictions2, type + "opposite");
				System.out.println("opposite thing: " + type+ "opposite");
				System.out.println("change to value shenanigangs: " + changeToValue);
				typeChangeToValue = typeSecondRestriction;
			}
//			System.out.println("ChangeToValue: " + changeToValue);
//			System.out.println("TypeChangeToValue: " + typeChangeToValue);
			this.changeValue(sentenceArray, toChange, changeToValue, typeChangeToValue);
		}
		System.out.println("Num Restrictions Sentence Array: ");
		for (Pair<String, JsonArray> a : sentenceArray) {
			System.out.println(a.getA() + " " + a.getB());
		}
		return sentenceArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyRestrictions(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		for(Pair<String, String> restriction : this.getGrammar().getRestrictions()) {
			int dotPointA = restriction.getA().indexOf(".");
			int dotPointB = restriction.getB().indexOf(".");
			String restrictionType = restriction.getA().substring(dotPointA + 1, restriction.getA().length());
			String elementA = "";
			String elementB = "";
			Pair<String, String> pair = null;
			System.out.println("BEFORE!!!");
			for (Pair<String, JsonArray> a : sentenceArray) {
				System.out.println(a.getA() + " ");
			}
			switch (restrictionType) {
				case "num": 
					elementA = restriction.getA().substring(0, dotPointA);
					elementB = restriction.getB().substring(0, dotPointB);
					pair = new Pair<String, String>(elementA, elementB);
					sentenceArray = applyRestrictions(pair, sentenceArray, null, "num");
					break;
				case "gen": 
					elementA = restriction.getA().substring(0, dotPointA);
					elementB = restriction.getB().substring(0, dotPointB);
					pair = new Pair<String, String>(elementA, elementB);
					sentenceArray = applyRestrictions(pair, sentenceArray, null, "gen");
					break;
			}
			System.out.println("After!!!");
			for (Pair<String, JsonArray> a : sentenceArray) {
				System.out.println(a + " ");
			}
		}
		return sentenceArray;
	}
	
	public boolean emptySentenceArray(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		for (Pair<String, JsonArray> a : sentenceArray) {
			if (a == null) return true;
		}
		return false;
	}
	
	protected ArrayList<String> getGrammarTypes() {
		ArrayList<String> parsedGrammarTypes = new ArrayList<String>();
		ArrayList<String> grammarTypes = this.getGrammar().getGrammar().get("keys");
		for (int i = 0; i < grammarTypes.size(); i++) {
			parsedGrammarTypes.add(this.returnParseString(grammarTypes.get(i), "_"));
		}
		return parsedGrammarTypes;
	}
	
	public String returnParseString(String string, String element) {
		System.out.println("String: " + string);
		System.out.println("Element: " + element);
		return string.substring(0, string.indexOf(element));
	}
	
	public abstract String getRandomSentence();
	
	public GrammarIndividual getGrammar() {
		return grammar;
	}

	public void setGrammar(GrammarIndividual grammar) {
		this.grammar = grammar;
	}

	public JsonObject getWordsGrammar() {
		return wordsGrammar;
	}

	public void setWordsGrammar(JsonObject wordsGrammar) {
		this.wordsGrammar = wordsGrammar;
	}
}
