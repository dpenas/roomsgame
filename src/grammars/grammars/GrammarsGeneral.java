package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import net.slashie.util.Pair;

public class GrammarsGeneral {
	private ArrayList<Float> probabilities = new ArrayList<Float>();
	private JsonObject generalGrammar;
	private ArrayList<GrammarIndividual> invidivualGrammars;
	
	public GrammarsGeneral(JsonObject generalGrammar) {
		this.generalGrammar = generalGrammar;
		this.probabilities = GrammarsOperational.initializeProbabilities(GrammarsRetrieval.getNumberGrammars(generalGrammar));
		this.setInvidivualGrammars(this.intializeInidividualGrammars());
	}
	
	public GrammarIndividual getRandomGrammar() {
		GrammarIndividual grammar;
		Pair<Integer, JsonObject> result = GrammarsOperational.selectGrammar(this.getGeneralGrammar(), this.getProbabilities());
		this.setProbabilities(GrammarsOperational.recalculateProbabilities(this.getProbabilities(), result.getA()));
		if (!this.getIndividualGrammars().contains(result.getB())) {
			grammar = new GrammarIndividual(result.getB());
			this.getIndividualGrammars().add(result.getA(), grammar);
		} else {
			grammar = this.getIndividualGrammars().get(result.getA());
		}
		return grammar;
	}

	public ArrayList<Float> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(ArrayList<Float> probabilities) {
		this.probabilities = probabilities;
	}
	
	private ArrayList<GrammarIndividual> intializeInidividualGrammars() {
		ArrayList<GrammarIndividual> individualGrammars = new ArrayList<GrammarIndividual>();
		int numberGrammars = GrammarsRetrieval.getNumberGrammars(this.getGeneralGrammar());
		for (int i = 0; i < numberGrammars; i++) {
			individualGrammars.add(null);
		}
		return individualGrammars;
	}

	public JsonObject getGeneralGrammar() {
		return generalGrammar;
	}

	public void setGeneralGrammar(JsonObject generalGrammar) {
		this.generalGrammar = generalGrammar;
	}
	
	public ArrayList<GrammarIndividual> getIndividualGrammars() {
		return this.invidivualGrammars;
	}

	public void setInvidivualGrammars(ArrayList<GrammarIndividual> invidivualGrammars) {
		this.invidivualGrammars = invidivualGrammars;
	}
}
