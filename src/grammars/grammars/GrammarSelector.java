package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import items.Item;
import net.slashie.util.Pair;
import util.RandUtil;

public class GrammarSelector {
	
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
	
	public String getRandomSentence() {
		String sentence = "";
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		for(int i = 0; i < sentenceArray.size(); i++) {
			sentence += sentenceArray.get(i).getA() + " ";
		}
		// TODO: Get words from grammar
		// TODO: Apply restrictions
		// TODO: Return sentences
//		item.getName();
//		grammar.getRestrictions();
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
