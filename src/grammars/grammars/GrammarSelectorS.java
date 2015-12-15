package grammars.grammars;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import grammars.parsing.JSONParsing;
import items.Item;
import net.slashie.util.Pair;
import util.RandUtil;

public class GrammarSelectorS extends GrammarSelector {

	private ArrayList<Item> items;
	private String type;
	private ArrayList<Pair<String, JsonArray>> verbs;
	private ArrayList<GrammarSelectorNP> grammarsNP = new ArrayList<GrammarSelectorNP>();
	private ArrayList<ArrayList<Pair<String, JsonArray>>> grammarsNPPair = new ArrayList<ArrayList<Pair<String, JsonArray>>>();
	private JsonObject grammarObj;

	public GrammarSelectorS(GrammarIndividual grammar, JsonObject wordsGrammar, ArrayList<Item> items, String type) throws JsonIOException, JsonSyntaxException, FileNotFoundException, InstantiationException, IllegalAccessException {
		super(grammar, wordsGrammar);
		JsonParser parser = new JsonParser();
		this.grammarObj = parser.parse(new FileReader("./src/grammars/english/objectGrammarTest.json")).getAsJsonObject();
		this.verbs = WordsGrammar.getVerbs(wordsGrammar);
		this.items = items;
		this.analyseGrammar();
	}
	
	private void analyseGrammar() throws InstantiationException, IllegalAccessException {
		System.out.println("WORKING ON THIS");
		for (int i = 0; i < this.getGrammar().getGrammar().get("keys").size(); i++) {
			String value = this.getGrammar().getGrammar().get("keys").get(i);
			String typeValue = this.returnParseString(value, "_");
			if (!typeValue.equals("V")) {
				System.out.println("TypeValue: " + typeValue);
				this.getRandomNP(typeValue);
			}
		}
		System.out.println("FINISHED WORKING ON THIS");
	}
	
	private Pair<String, JsonArray> getRandomVerb() {
		if (this.getVerbs().size() > 0) {
			return this.getVerbs().get(RandUtil.RandomNumber(0, this.getVerbs().size()));
		}
		return null;
	}
	
	private Pair<String, JsonArray> getRandomNP(String type) {
		this.setGrammarsNP(new ArrayList<GrammarSelectorNP>());
		this.setGrammarsNPPair(new ArrayList<ArrayList<Pair<String, JsonArray>>>());
		JsonParser parser = new JsonParser();
		JsonObject rootObj = null;
		try {
			rootObj = parser.parse(new FileReader("./src/grammars/english/objectGrammar.json")).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		rootObj = JSONParsing.getElement(rootObj, type).getAsJsonObject();
		for (int i = 0; i < this.getItems().size(); i++) {
			GrammarsGeneral grammarGeneral = new GrammarsGeneral(rootObj);
			this.getGrammarsNP().add(new GrammarSelectorNP(grammarGeneral.getRandomGrammar(), this.getWordsGrammar(), this.getItems().get(i), type));
			this.getGrammarsNPPair().add(this.getGrammarsNP().get(i).getRandomSentencePair());
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
	
	protected ArrayList<Pair<String, JsonArray>> changeValue(ArrayList<Pair<String, JsonArray>> sentenceArray, String valueToChange, 
			String changeToValue, String typeChangeToValue){
		System.out.println("Value to change: " + valueToChange);
		System.out.println("Change to: " + changeToValue);
		System.out.println("TypeChangeToValue: " + typeChangeToValue);
		ArrayList<Pair<String, JsonArray>> selectedTypeWord = new ArrayList<Pair<String, JsonArray>>();
		switch (typeChangeToValue) {
			case "V" :
				selectedTypeWord = this.getVerbs();
				break;
			default : 
				return this.getGrammarsNP().get(0).changeValue(sentenceArray, valueToChange, changeToValue, typeChangeToValue);
		}
		
		for (int i = 0; i < selectedTypeWord.size(); i++) {
			if (selectedTypeWord.get(i).getA().equals(changeToValue)) {
				Pair<String, JsonArray> newPair = selectedTypeWord.get(i);
				for (int j = 0; j < sentenceArray.size(); j++) {
					System.out.println("COMPARISON: " + sentenceArray.get(j).getA());
					if (sentenceArray.get(j).getA().equals(valueToChange)) {
						sentenceArray.set(j, newPair);
					}
				}
			}
		}
		for (Pair<String,JsonArray> a : sentenceArray) {
			System.out.println("Sentence Array a: " + a.getA());
			System.out.println("Sentence Array b: " + a.getB());
		}
		return sentenceArray;
	}
	
	public String getRandomSentence() {
		String sentence = "";
		int npCount = 0;
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		sentenceArray = this.applyRestrictions(sentenceArray);
		for(int i = 0; i < sentenceArray.size(); i++) {
			Pair<String, JsonArray> pair = sentenceArray.get(i);
			if (pair != null) {
				sentence += " " + pair.getA();
			} else {
				ArrayList<Pair<String, JsonArray>> arrayNPPair = this.getGrammarsNPPair().get(npCount);
				for (int z = 0; z < arrayNPPair.size(); z++){
					sentence += " " + arrayNPPair.get(z).getA();
				}
				npCount++;
			}
		}
		
		return sentence;
	}
	
	private ArrayList<Pair<String, JsonArray>> applyRestrictionsSNP(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		ArrayList<Pair<String, JsonArray>> newSentenceArray = new ArrayList<Pair<String, JsonArray>>();
		ArrayList<Pair<String, JsonArray>> returnSentenceArray = new ArrayList<Pair<String, JsonArray>>();
		for (Pair<String, JsonArray> pair : sentenceArray) {
			if (pair != null) {
				newSentenceArray.add(pair);
			}
		}
//		System.out.println("Printing new Sentence Array: ");
//		for (Pair<String, JsonArray> pair : newSentenceArray) {
//			System.out.println(pair.getA());
//			System.out.println(pair.getB());
//		}
		// TODO: Change this
		newSentenceArray.add(this.getGrammarsNPPair().get(0).get(2));
		newSentenceArray.set(1, newSentenceArray.get(0));
		newSentenceArray.set(0, this.getGrammarsNPPair().get(0).get(2));
		
		for(Pair<String, String> restriction : this.getGrammar().getRestrictions()) {
			int dotPointA = restriction.getA().indexOf(".");
			int dotPointB = restriction.getB().indexOf(".");
			String restrictionType = restriction.getA().substring(dotPointA + 1, restriction.getA().length());
			switch (restrictionType) {
				case "num": 
					String elementA = restriction.getA().substring(0, dotPointA);
					String elementB = restriction.getB().substring(0, dotPointB);
					Pair<String, String> pair = new Pair<String, String>(elementA, elementB);
					returnSentenceArray = applyNumRestrictions(pair, newSentenceArray);
					// TODO: Change this so we don't have to set with "1"
					for(Pair<String, JsonArray> a : returnSentenceArray) {
						if (this.getVerbs().contains(a)) {
							returnSentenceArray.set(1, a);
						}
					}
					returnSentenceArray.set(0, null);
					returnSentenceArray.add(null);
					break;
			}
		}
		return returnSentenceArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyRestrictions(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		if (this.emptySentenceArray(sentenceArray)) {
			return this.applyRestrictionsSNP(sentenceArray);
		}
		return null;
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

	public ArrayList<ArrayList<Pair<String, JsonArray>>> getGrammarsNPPair() {
		return grammarsNPPair;
	}

	public void setGrammarsNPPair(ArrayList<ArrayList<Pair<String, JsonArray>>> grammarsNPPair) {
		this.grammarsNPPair = grammarsNPPair;
	}

	public JsonObject getGrammarObj() {
		return grammarObj;
	}

	public void setGrammarObj(JsonObject grammarObj) {
		this.grammarObj = grammarObj;
	}
	
}

