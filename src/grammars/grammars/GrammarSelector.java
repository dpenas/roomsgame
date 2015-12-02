package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import items.Item;
import net.slashie.util.Pair;
import util.RandUtil;

public class GrammarSelector {
	private static final String[] WORD_IMPORTANCE = {"V", "N", "ADJ", "DET"};
	private GrammarIndividual grammar;
	private JsonObject wordsGrammar;
	private Item item;
	private ArrayList<Pair<String, JsonArray>> adjectives;
	private Pair<String, JsonArray> names;
	private ArrayList<Pair<String, JsonArray>> determinants;
	
	public GrammarSelector(GrammarIndividual grammar, JsonObject wordsGrammar, Item item) {
		this.grammar = grammar;
		this.wordsGrammar = wordsGrammar;
		this.item = item;
		this.adjectives = WordsGrammar.getAdjectives(wordsGrammar, item.getAdjectives());
		this.names = WordsGrammar.getName(wordsGrammar, item.getName());
		this.determinants = WordsGrammar.getDeterminant(wordsGrammar);
	}
	
	private ArrayList<Pair<String, JsonArray>> fillWords() {
		ArrayList<Pair<String, JsonArray>> resultArray = new ArrayList<Pair<String, JsonArray>>();
		for (String value : this.getGrammar().getTypeWordGrammar()) {
			switch (value) {
				case "DET" : resultArray.add(getRandomDeterminant());
					break;
				case "ADJ" : resultArray.add(getRandomAdjective());
					break;
				case "N" : resultArray.add(getNames());
					break;
			}
		}
		return resultArray;
	}
	
	private Pair<String, JsonArray> getRandomDeterminant() {
		if (this.getDeterminants().size() > 0) {
			return this.getDeterminants().get(RandUtil.RandomNumber(0, this.getDeterminants().size()));
		}
		return null;
	}
	
	private Pair<String, JsonArray> getRandomAdjective() {
		if (this.getAdjectives().size() > 0) {
			return this.getAdjectives().get(RandUtil.RandomNumber(0, this.getAdjectives().size()));
		}
		return null;
	}
	
	private String getImportantRestriction(String value1, String value2) {
		for (int i = 0; i < WORD_IMPORTANCE.length; i++) {
			if (WORD_IMPORTANCE[i].equals(value1)) {
				return value2;
			}
			if (WORD_IMPORTANCE[i].equals(value2)) {
				return value1;
			}
		}
		return "";
	}
	
	private ArrayList<Pair<String, JsonArray>> applyNumRestrictions(Pair<String, String> restriction, ArrayList<Pair<String, JsonArray>> sentenceArray) {
		for (Pair<String, JsonArray> sentence : sentenceArray) {
			System.out.println(sentence.getB());
		}
		ArrayList<String> grammar = this.getGrammar().getGrammar().get("keys");
		String firstType = restriction.getA();
		String secondType = restriction.getB();
		String word1 = sentenceArray.get(grammar.indexOf(firstType)).getA();
		JsonArray restrictions1 = sentenceArray.get(grammar.indexOf(firstType)).getB();
		String word2 = sentenceArray.get(grammar.indexOf(secondType)).getA();
		JsonArray restrictions2 = sentenceArray.get(grammar.indexOf(secondType)).getB();
		String value1 = JSONParsing.getElement(restrictions1, "num");
		String value2 = JSONParsing.getElement(restrictions2, "num");
		if (value1 != value2) {
			String toChange = getImportantRestriction(value1, value2);
			if (toChange.equals(value1)) {
				String changeToValue = JSONParsing.getElement(restrictions1, "numopposite");
			} else {
				String changeToValue = JSONParsing.getElement(restrictions2, "numopposite");
			}
			// TODO: Remove pair from sentenceArray
			// TODO: Introduce new pair from sentenceArray
		}
		return sentenceArray;
		
		
	}
	
	private void applyRestrictions(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		for(Pair<String, String> restriction : this.getGrammar().getRestrictions()) {
			int dotPointA = restriction.getA().indexOf(".");
			int dotPointB = restriction.getB().indexOf(".");
			String restrictionType = restriction.getA().substring(dotPointA + 1, restriction.getA().length());
			switch (restrictionType) {
				case "num": 
					String elementA = restriction.getA().substring(0, dotPointA);
					String elementB = restriction.getB().substring(0, dotPointB);
					Pair<String, String> pair = new Pair<String, String>(elementA, elementB);
					applyNumRestrictions(pair, sentenceArray);
					break;
			}
		}
	}
	
	public String getRandomSentence() {
		String sentence = "";
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		this.applyRestrictions(sentenceArray);
//		for(int i = 0; i < sentenceArray.size(); i++) {
//			sentence += sentenceArray.get(i).getA() + " ";
//		}
//		item.getName();
//		
		return sentence;
	}

	public GrammarIndividual getGrammar() {
		return grammar;
	}

	public void setGrammar(GrammarIndividual grammar) {
		this.grammar = grammar;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ArrayList<Pair<String, JsonArray>> getAdjectives() {
		return adjectives;
	}

	public void setAdjectives(ArrayList<Pair<String, JsonArray>> adjectives) {
		this.adjectives = adjectives;
	}

	public Pair<String, JsonArray> getNames() {
		return names;
	}

	public void setNames(Pair<String, JsonArray> names) {
		this.names = names;
	}

	public ArrayList<Pair<String, JsonArray>> getDeterminants() {
		return determinants;
	}

	public void setDeterminants(ArrayList<Pair<String, JsonArray>> determinants) {
		this.determinants = determinants;
	}
}
