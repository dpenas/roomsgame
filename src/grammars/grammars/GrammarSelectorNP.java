package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.slashie.util.Pair;
import util.RandUtil;

public class GrammarSelectorNP extends GrammarSelector {
	
	private PrintableObject name;
	private String type;
	private ArrayList<Pair<String, JsonArray>> allPrepositions;
	private ArrayList<Pair<String, JsonArray>> prepositions;
	private ArrayList<Pair<String, JsonArray>> adjectives;
	private ArrayList<Pair<String, JsonArray>> allAdjectives;
	private ArrayList<Pair<String, JsonArray>> names;
	private ArrayList<Pair<String, JsonArray>> determinants;

	public GrammarSelectorNP(GrammarIndividual grammar, JsonObject wordsGrammar, PrintableObject name, String type) {
		super(grammar, wordsGrammar);
		this.name = name;
		this.type = type;
		this.prepositions = WordsGrammar.getPrepositions(wordsGrammar, name.getPrepositions());
		this.allPrepositions = WordsGrammar.getAllPrepositions(wordsGrammar);
		this.allAdjectives = WordsGrammar.getAllAdjectives(wordsGrammar);
		this.adjectives = WordsGrammar.getAdjectives(wordsGrammar, name.getAdjectives());
		this.names = WordsGrammar.getName(wordsGrammar, name.getName());
		this.determinants = WordsGrammar.getDeterminant(wordsGrammar);
	}
	
	private Pair<String, JsonArray> getRandomDeterminant() {
		if (this.getDeterminants().size() > 0) {
			return this.getDeterminants().get(RandUtil.RandomNumber(0, this.getDeterminants().size()));
		}
		return null;
	}
	
	public Pair<String, JsonArray> getRandomAdjective() {
		if (this.getAdjectives().size() > 0) {
			return this.getAdjectives().get(RandUtil.RandomNumber(0, this.getAdjectives().size()));
		}
		return null;
	}
	
	public Pair<String, JsonArray> getRandomPreposition() {
		if (this.getPrepositions().size() > 0) {
			return this.getPrepositions().get(RandUtil.RandomNumber(0, this.getPrepositions().size()));
		}
		return null;
	}
	
	protected ArrayList<Pair<String, JsonArray>> fillWords() {
		ArrayList<Pair<String, JsonArray>> resultArray = new ArrayList<Pair<String, JsonArray>>();
		for (String value : this.getGrammar().getTypeWordGrammar()) {
			System.out.println("Value: " + value);
			switch (value) {
				case "DET" : resultArray.add(getRandomDeterminant());
					break;
				case "PREP" : resultArray.add(getRandomPreposition());
					break;
				case "ADJ" : resultArray.add(getRandomAdjective());
					break;
					// TODO: This get(0) might not be the best idea
				case "N" : resultArray.add(getNames().get(0));
					break;
			}
		}
		return resultArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> changeValue(ArrayList<Pair<String, JsonArray>> sentenceArray, String valueToChange, String changeToValue, String typeChangeToValue){
		System.out.println("Value to change!!: " + valueToChange);
		System.out.println("Change to!!: " + changeToValue);
		ArrayList<Pair<String, JsonArray>> selectedTypeWord = new ArrayList<Pair<String, JsonArray>>();
		switch (typeChangeToValue) {
			case "DET" :
				selectedTypeWord = this.getDeterminants();
				break;
			case "N" :
				selectedTypeWord = this.getNames();
				break;
			case "ADJECTIVE":
			case "ADJ":
				selectedTypeWord = this.getAllAdjectives();
				break;
		}
		
//		System.out.println("SelectedTypeWord.get(A): " + selectedTypeWord.get(0).getA());
//		System.out.println("sentenceArray.get(0).getA(): " + sentenceArray.get(0).getA());
		for (int i = 0; i < selectedTypeWord.size(); i++) {
			System.out.println("changeToValue: "+ changeToValue);
			System.out.println("selectedTypeWord.get(i).getA(): "+ selectedTypeWord.get(i).getA());
			if (selectedTypeWord.get(i).getA().equals(changeToValue)) {
				Pair<String, JsonArray> newPair = selectedTypeWord.get(i); 
				for (int j = 0; j < sentenceArray.size(); j++) {
					if (sentenceArray.get(j).getA().equals(valueToChange)) {
						sentenceArray.set(j, newPair);
					}
				}
			}
		}
		
		return sentenceArray;
	}
	
	public String getRandomSentence() {
		String sentence = "";
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		sentenceArray = this.applyRestrictions(sentenceArray);
		for(int i = 0; i < sentenceArray.size(); i++) {
			sentence += sentenceArray.get(i).getA() + " ";
		}
		return sentence;
	}
	
	public ArrayList<Pair<String, JsonArray>> getRandomSentencePair() {
		System.out.println(this.getGrammar());
		System.out.println(this.getName());
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		for (Pair<String, JsonArray> a : sentenceArray) {
			if (a != null) {
				System.out.println(a.getA());
				System.out.println(a.getB());
			}
		}
		sentenceArray = this.applyRestrictions(sentenceArray);
		return sentenceArray;
	}
	
	public int getSelectedNamePos() {
		ArrayList<String> elements = this.getGrammar().getGrammar().get("keys");
		for (int i = 0; i < elements.size(); i++) {
			if (this.returnParseString(elements.get(i), "_").equals("N")) {
				return i;
			}
		}
		return 0;
	}
	
	public boolean isPreposition(String givenPreposition) {
		for (Pair<String, JsonArray> preposition : this.getAllPrepositions()) {
			if (preposition.getA().equals(givenPreposition)) {
				return true;
			}
		}
		return false;
	}

	public PrintableObject getName() {
		return name;
	}

	public void setName(PrintableObject name) {
		this.name = name;
	}

	public ArrayList<Pair<String, JsonArray>> getAdjectives() {
		return adjectives;
	}

	public void setAdjectives(ArrayList<Pair<String, JsonArray>> adjectives) {
		this.adjectives = adjectives;
	}

	public ArrayList<Pair<String, JsonArray>> getNames() {
		return names;
	}

	public void setNames(ArrayList<Pair<String, JsonArray>> names) {
		this.names = names;
	}

	public ArrayList<Pair<String, JsonArray>> getDeterminants() {
		return determinants;
	}

	public void setDeterminants(ArrayList<Pair<String, JsonArray>> determinants) {
		this.determinants = determinants;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Pair<String, JsonArray>> getAllAdjectives() {
		return allAdjectives;
	}

	public void setAllAdjectives(ArrayList<Pair<String, JsonArray>> allAdjectives) {
		this.allAdjectives = allAdjectives;
	}

	public ArrayList<Pair<String, JsonArray>> getAllPrepositions() {
		return allPrepositions;
	}

	public void setAllPrepositions(ArrayList<Pair<String, JsonArray>> allPrepositions) {
		this.allPrepositions = allPrepositions;
	}

	public ArrayList<Pair<String, JsonArray>> getPrepositions() {
		return prepositions;
	}

	public void setPrepositions(ArrayList<Pair<String, JsonArray>> prepositions) {
		this.prepositions = prepositions;
	}

}
