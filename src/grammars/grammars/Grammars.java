package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import net.slashie.util.Pair;

public class Grammars {
	private ArrayList<Float> probabilities = new ArrayList<Float>();
	private JsonObject grammar;
	
	public Grammars(JsonObject grammar) {
		this.grammar = grammar;
		this.probabilities = GrammarsOperational.initializeProbabilities(GrammarsRetrieval.getNumberGrammars(grammar));
	}
	
	public JsonObject getRandomGrammar() {
		Pair<Integer, JsonObject> result = GrammarsOperational.selectGrammar(this.getGrammar(), this.getProbabilities());
		this.setProbabilities(GrammarsOperational.recalculateProbabilities(this.getProbabilities(), result.getA()));
		return result.getB();
	}

	public ArrayList<Float> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(ArrayList<Float> probabilities) {
		this.probabilities = probabilities;
	}

	public JsonObject getGrammar() {
		return grammar;
	}

	public void setGrammar(JsonObject grammar) {
		this.grammar = grammar;
	}
}
