package grammars.grammars;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import items.Item;
import net.slashie.util.Pair;
import util.RandUtil;

public class GrammarSelectorS extends GrammarSelector {

	private ArrayList<Item> items;
	private String type;
	private ArrayList<Pair<String, JsonArray>> verbs;
	private ArrayList<GrammarSelectorNP> grammarsNP = new ArrayList<GrammarSelectorNP>();

	public GrammarSelectorS(GrammarIndividual grammar, JsonObject wordsGrammar, ArrayList<Item> items, String type) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		super(grammar, wordsGrammar);
		this.verbs = WordsGrammar.getVerbs(wordsGrammar);
		this.items = items;
	}
	
	private Pair<String, JsonArray> getRandomVerb() {
		if (this.getVerbs().size() > 0) {
			return this.getVerbs().get(RandUtil.RandomNumber(0, this.getVerbs().size()));
		}
		return null;
	}
	
	private Pair<String, JsonArray> getRandomNP() {
		for (Item item : this.getItems()) {
			this.getGrammarsNP().add(new GrammarSelectorNP(new GrammarIndividual(this.getGrammar().getObject()), this.getWordsGrammar(), item));
		}
		return null;
	}
	
	protected ArrayList<Pair<String, JsonArray>> fillWords() {
		ArrayList<Pair<String, JsonArray>> resultArray = new ArrayList<Pair<String, JsonArray>>();
		for (String value : this.getGrammar().getTypeWordGrammar()) {
			switch (value) {
				case "V" : 
					resultArray.add(getRandomVerb());
					break;
				default: resultArray.add(null);
			}
		}
		return resultArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> changeValue(ArrayList<Pair<String, JsonArray>> sentenceArray, String valueToChange, String changeToValue, String typeChangeToValue){
		System.out.println("Value to change: " + valueToChange);
		System.out.println("Change to: " + changeToValue);
		ArrayList<Pair<String, JsonArray>> selectedTypeWord = new ArrayList<Pair<String, JsonArray>>();
		switch (typeChangeToValue) {
			case "V" :
				selectedTypeWord = this.getVerbs();
				break;
		}
		
		for (int i = 0; i < selectedTypeWord.size(); i++) {
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
		this.getRandomNP();
		String sentence = "";
		int j = 0;
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		System.out.println(sentenceArray);
		sentenceArray = this.applyRestrictions(sentenceArray);
		for(int i = 0; i < sentenceArray.size(); i++) {
//			System.out.println(sentenceArray.get(i));
			if (sentenceArray.get(i) == null) {
				sentence += " " + this.getGrammarsNP().get(j).getRandomSentence();
				j++;
			} else {
				sentence += sentenceArray.get(i).getA() + " ";
			}
		}
		
		return sentence;
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyRestrictions(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		for(Pair<String, String> restriction : this.getGrammar().getRestrictions()) {
			System.out.println(restriction);
			System.out.println(restriction.getA());
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

	public ArrayList<Pair<String, JsonArray>> getVerbs() {
		return verbs;
	}

	public void setVerbs(ArrayList<Pair<String, JsonArray>> verbs) {
		this.verbs = verbs;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<GrammarSelectorNP> getGrammarsNP() {
		return grammarsNP;
	}

	public void setGrammarsNP(ArrayList<GrammarSelectorNP> grammarsNP) {
		this.grammarsNP = grammarsNP;
	}
	
}

