package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import net.slashie.util.Pair;

public abstract class GrammarSelector {
	private static String[] WORD_IMPORTANCE = {"NP", "N", "ADJ", "DET", "V"};
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
	
	protected ArrayList<Pair<String, JsonArray>> applyNumRestrictions(Pair<String, String> restriction, ArrayList<Pair<String, JsonArray>> sentenceArray) {
//		for (Pair<String, JsonArray> sentence : sentenceArray) {
//			System.out.println(sentence.getB());
//		}
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
		JsonArray restrictions1 = sentenceArray.get(grammar.indexOf(firstType)).getB();
		JsonArray restrictions2 = sentenceArray.get(grammar.indexOf(secondType)).getB();
//		System.out.println("restrictions1: " + restrictions1);
//		System.out.println("restrictions2: " + restrictions2);
		String value1 = JSONParsing.getElement(restrictions1, "translation");
		String value2 = JSONParsing.getElement(restrictions2, "translation");
		String value1Num = JSONParsing.getElement(restrictions1, "num");
		String value2Num = JSONParsing.getElement(restrictions2, "num");
		System.out.println("Value1Num: " + value1Num);
		System.out.println("Value2Num: " + value2Num);
		System.out.println("Value1: " + value1);
		System.out.println("Value2: " + value2);
		if (!value1Num.equals(value2Num) && value1Num.length() > 0 && value2Num.length() > 0) {
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
	
	public boolean emptySentenceArray(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		for (Pair<String, JsonArray> a : sentenceArray) {
			if (a == null) return true;
		}
		return false;
	}
	
	public String returnParseString(String string, String element) {
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
