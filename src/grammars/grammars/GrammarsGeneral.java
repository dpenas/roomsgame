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
		this.setInvidivualGrammars(this.initializeInidividualGrammars());
	}
	
	public GrammarIndividual getRandomGrammar() {
		System.out.println("Number of grammars INI: " + GrammarsRetrieval.getNumberGrammars(generalGrammar));
		GrammarIndividual grammar;
		Pair<Integer, JsonObject> result = GrammarsOperational.selectGrammar(this.getGeneralGrammar(), this.getProbabilities());
		System.out.println("Result A!!: " + result.getA());
		System.out.println("Result B!!: " + result.getB());
		this.setProbabilities(GrammarsOperational.recalculateProbabilities(this.getProbabilities(), result.getA()));
		System.out.println("Probabilities outside: " + this.getProbabilities().size());
		if (!this.getIndividualGrammars().contains(result.getB())) {
			grammar = new GrammarIndividual(result.getB());
			this.getIndividualGrammars().add(result.getA(), grammar);
		} else {
			grammar = this.getIndividualGrammars().get(result.getA());
		}
		System.out.println("Number of grammars END: " + GrammarsRetrieval.getNumberGrammars(generalGrammar));
		return grammar;
	}

	public ArrayList<Float> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(ArrayList<Float> probabilities) {
		this.probabilities = probabilities;
	}
	
	private ArrayList<GrammarIndividual> initializeInidividualGrammars() {
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
