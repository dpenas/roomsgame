package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import items.Item;
import net.slashie.util.Pair;

public class GrammarSelector {
	
	private GrammarIndividual grammar;
	private JsonObject wordsGrammar;
	private Item item;
	private ArrayList<Pair<String, JsonArray>> adjectives;
	private JsonArray names;
	private Pair<String, JsonArray> determinants;
	
	public GrammarSelector(GrammarIndividual grammar, JsonObject wordsGrammar, Item item) {
		this.grammar = grammar;
		this.wordsGrammar = wordsGrammar;
		this.item = item;
		this.adjectives = WordsGrammar.getAdjectives(wordsGrammar, item.getAdjectives());
		this.names = WordsGrammar.getName(wordsGrammar, item.getName());
		this.determinants = WordsGrammar.getDeterminant(wordsGrammar);
	}
	
	private void fillWords() {
		System.out.println(this.getGrammar());
	}
	
	public String getRandomSentence() {
		String sentence = "";
		this.fillWords();
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

	public JsonArray getNames() {
		return names;
	}

	public void setNames(JsonArray names) {
		this.names = names;
	}

	public Pair<String, JsonArray> getDeterminants() {
		return determinants;
	}

	public void setDeterminants(Pair<String, JsonArray> determinants) {
		this.determinants = determinants;
	}
}
