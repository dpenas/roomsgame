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
import net.slashie.util.Pair;
import util.RandUtil;

public class GrammarSelectorS extends GrammarSelector {
	
	private String[] typesNPGrammarComplete = {"GENERAL_N", "SIMPLE_N"};
	private String[] typesNPGrammar = {"GENERAL", "SIMPLE"};

	private ArrayList<PrintableObject> names;
	private String type;
	private ArrayList<Pair<String, JsonArray>> verbs;
	private ArrayList<GrammarSelectorNP> grammarsNP = new ArrayList<GrammarSelectorNP>();
	private ArrayList<ArrayList<Pair<String, JsonArray>>> grammarsNPPair = new ArrayList<ArrayList<Pair<String, JsonArray>>>();
	private JsonObject grammarObj;

	public GrammarSelectorS(GrammarIndividual grammar, JsonObject wordsGrammar, ArrayList<PrintableObject> names, String type) throws JsonIOException, JsonSyntaxException, FileNotFoundException, InstantiationException, IllegalAccessException {
		super(grammar, wordsGrammar);
		JsonParser parser = new JsonParser();
		this.type = type;
		this.grammarObj = parser.parse(new FileReader("./src/grammars/english/objectGrammarTest.json")).getAsJsonObject();
		this.verbs = WordsGrammar.getVerbs(wordsGrammar, type);
		this.names = names;
		this.analyseGrammar();
	}
	
	private void analyseGrammar() throws InstantiationException, IllegalAccessException {
		int namePos = 0;
		this.setGrammarsNP(new ArrayList<GrammarSelectorNP>());
		this.setGrammarsNPPair(new ArrayList<ArrayList<Pair<String, JsonArray>>>());
		int endIteration;
		if (type == "DESCITEM") {
			endIteration = this.getNames().size() + 1;
		} else {
			endIteration = this.getGrammar().getGrammar().get("keys").size();
		}
		for (int i = 0; i < endIteration; i++) {
			int selectedValue = i;
			if (i >= this.getGrammar().getGrammar().get("keys").size()) {
				selectedValue = this.getGrammar().getGrammar().get("keys").size() - 1;
			}
			String value = this.getGrammar().getGrammar().get("keys").get(selectedValue);
			String typeValue = this.returnParseString(value, "_");
			if (!typeValue.equals("V")) {
				System.out.println("NamePos naming thing: " + namePos);
				this.getRandomNP(typeValue, namePos);
				namePos++;
			}
		}
	}
	
	private Pair<String, JsonArray> getRandomVerb() {
		if (this.getVerbs().size() > 0) {
			return this.getVerbs().get(RandUtil.RandomNumber(0, this.getVerbs().size()));
		}
		return null;
	}
	
	private Pair<String, JsonArray> getRandomNP(String type, int namePos) {
		if (namePos > this.getNames().size() - 1) {
			namePos = this.getNames().size() - 1;
		}
		JsonParser parser = new JsonParser();
		JsonObject rootObj = null;
		try {
			rootObj = parser.parse(new FileReader("./src/grammars/english/objectGrammar.json")).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		rootObj = JSONParsing.getElement(rootObj, type).getAsJsonObject();
		System.out.println("namePos is this: !: " + namePos);
		System.out.println("We are going to use this item: !: " + this.getNames().get(namePos).getName());
		for (PrintableObject name : this.getNames()) {
			System.out.println("Naming thingy: " + name.getName());
		}
		GrammarsGeneral grammarGeneral = new GrammarsGeneral(rootObj);
		this.getGrammarsNP().add(new GrammarSelectorNP(grammarGeneral.getRandomGrammar(), this.getWordsGrammar(), this.getNames().get(namePos), type));
		System.out.println("RandomSentencePair: " + this.getGrammarsNP().get(this.getGrammarsNP().size() - 1).getRandomSentencePair());
		this.getGrammarsNPPair().add(this.getGrammarsNP().get(this.getGrammarsNP().size() - 1).getRandomSentencePair());
		return null;
	}
	
	protected ArrayList<Pair<String, JsonArray>> fillWords() {
		ArrayList<Pair<String, JsonArray>> resultArray = new ArrayList<Pair<String, JsonArray>>();
		int endIteration;
		if (type == "DESCITEM") {
			endIteration = this.getNames().size() + 1;
		} else {
			endIteration = this.getGrammar().getTypeWordGrammar().size();
		}
		System.out.println("ENDITERATION: " + endIteration);
		for (int i = 0; i < endIteration; i++) {
			int selectedTypeWordGrammar = i;
			if (selectedTypeWordGrammar >= this.getGrammar().getTypeWordGrammar().size()) {
				selectedTypeWordGrammar = this.getGrammar().getTypeWordGrammar().size() - 1;
			}
			System.out.println(selectedTypeWordGrammar);
			switch (this.getGrammar().getTypeWordGrammar().get(selectedTypeWordGrammar)) {
				case "V" : 
					resultArray.add(getRandomVerb());
					break;
				default: resultArray.add(null);
			}
		}
		System.out.println("WOWOWOWO");
		for (int j = 0; j < resultArray.size(); j++) {
			System.out.println(resultArray.get(j));
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
	
	private boolean containsArray(String element, String[] array) {
		for (String individualElement : array) {
			System.out.println("IndividualElement!! " + individualElement);
			System.out.println("Element!! " + individualElement);
			if (element.equals(individualElement)) {
				return true;
			}
		}
		return false;
	}
	
	private int numberWordIndefiniteNP(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		ArrayList<String> completeGrammars = this.getGrammar().getGrammar().get("keys");
		ArrayList<String> nameGrammars = this.getGrammar().getTypeWordGrammar();
		int valuesIndefinite = sentenceArray.size();
		int numNP = 0;
		for (int i = 0; i < completeGrammars.size(); i++) {
			if (this.containsArray(nameGrammars.get(i), this.typesNPGrammar)) {
				System.out.println("HEYasdasd!!");
				if (!this.containsArray(completeGrammars.get(i), this.typesNPGrammarComplete)) {
					System.out.println("lelele: " + this.getGrammarsNPPair().get(numNP).size());
					valuesIndefinite -= this.getGrammarsNPPair().get(numNP).size();
				}
				numNP++;
			} else {
				valuesIndefinite -= 1;
			}
		}
		System.out.println("ValuesIndefinite! " + valuesIndefinite);
		return valuesIndefinite;
	}
	
	public String getRandomSentence(boolean usePronoun, boolean useAnd) {
		try {
			this.analyseGrammar();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		String sentence = "";
		System.out.println("GrammarsNP!!!");
		for (int j = 0; j < this.getGrammarsNP().size(); j++) {
			System.out.println(this.getGrammarsNP().get(j));
		}
		System.out.println("GrammarsNP Pair!!!");
		for (int j = 0; j < this.getGrammarsNPPair().size(); j++) {
			System.out.println(this.getGrammarsNPPair().get(j));
		}
		
		System.out.println("this.getGrammar(): " + this.getGrammar().getTypeWordGrammar());
		
		System.out.println("this.getGrammar2(): " + this.getGrammar().getGrammar().get("keys"));
		
		ArrayList<String> grammarsTotal = this.getGrammar().getGrammar().get("keys");
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		sentenceArray = this.applyRestrictions(sentenceArray);
		int grammarNP = 0;
		int iniIndefiniteNP = 0;
		for (int i = 0; i < grammarsTotal.size(); i++) {
			int sizeNPPair = this.getGrammarsNPPair().get(grammarNP).size();
			int numberWordIndefinite = this.numberWordIndefiniteNP(sentenceArray);
			if (this.containsArray(this.getGrammar().getTypeWordGrammar().get(i), typesNPGrammar)) {
				if (this.getGrammar().getNumberGrammar().get(i).equals("N")) {
					int sizeDivision = numberWordIndefinite / sizeNPPair;
					for (int j = 0; j < sizeDivision; j++) {
						if (values.size() == 0) {
							values.add(iniIndefiniteNP + this.getGrammarsNPPair().get(grammarNP).size());
						} else {
							values.add(values.get(values.size() - 1) + sizeNPPair);
						}
					}
				} else {
					iniIndefiniteNP += this.getGrammarsNPPair().get(grammarNP).size();
				}
				grammarNP++;
			} else {
				iniIndefiniteNP++;
			}
		}
		boolean changed = false;
		for(int i = 0; i < sentenceArray.size(); i++) {
			Pair<String, JsonArray> pair = sentenceArray.get(i);
			if (values.contains(i) && (values.get(values.size() - 2)) == i) {
				JsonObject others = JSONParsing.getElement(this.getWordsGrammar(), "OTHERS").getAsJsonObject();
				JsonArray and = JSONParsing.getElement(others, "and").getAsJsonArray();
				String translationAnd = JSONParsing.getElement(and, "translation");
				sentence += " " + translationAnd + " " + pair.getA();
				changed = false;
			} else {
				if (values.contains(i)) {
					sentence += ", " + pair.getA() + " ";
					changed = true;
				} else {
					if (changed) {
						sentence += pair.getA();
					} else {
						sentence += " " + pair.getA();
					}
					changed = false;
				}
			}
		}
		if (usePronoun) {
			System.out.println("In testing!");
			String nameToGetPronounFrom = this.getGrammarsNP().get(0).getName().getName();
			String toChangeFor = "";
			String pronoun = JSONParsing.getElement(WordsGrammar.getName(this.getWordsGrammar(), nameToGetPronounFrom).get(0).getB(), "pronoun");
			if (useAnd) {
				JsonObject others = JSONParsing.getElement(this.getWordsGrammar(), "OTHERS").getAsJsonObject();
				JsonArray and = JSONParsing.getElement(others, "and").getAsJsonArray();
				String translationAnd = JSONParsing.getElement(and, "translation");
				toChangeFor += translationAnd;
			}
			toChangeFor += " " + pronoun + " ";
			String NPToDelete = "";
			for (Pair<String, JsonArray> word : this.getGrammarsNPPair().get(0)) {
				System.out.println("Word: " + word.getA());
				NPToDelete += word.getA() + " ";
			}
			System.out.println("NPToDelete: " + NPToDelete);
			sentence = sentence.replaceAll(NPToDelete, toChangeFor);
		}
		
		return sentence;
	}
	
	@Override
	public String getRandomSentence() {
		return this.getRandomSentence(false, false);
	}
	
	private ArrayList<Pair<String, JsonArray>> applyRestrictionsSNP(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		ArrayList<Pair<String, JsonArray>> newSentenceArray = new ArrayList<Pair<String, JsonArray>>();
		ArrayList<String> getGrammarTypes = this.getGrammarTypes();
		ArrayList<Integer> numItems = new ArrayList<Integer>();
		int iteration = 0;
		for(Pair<String, String> restriction : this.getGrammar().getRestrictions()) {
			System.out.println("Iteration: " + iteration);
			System.out.println("WORKING ON THIS: ");
			int NPgrammarCount = 0;
			/* We only execute this when there's no data on newSentenceArray, since in that case we already
			 have the information we need */
			int endIteration;
			if (type == "DESCITEM") {
				endIteration = this.getNames().size() + 1;
			} else {
				endIteration = this.getGrammarTypes().size();
			}
			if (newSentenceArray.size() == 0) {
				for (int i = 0; i < endIteration; i++) {
					int selectElement = i;
					if (i >= getGrammarTypes().size()) {
						selectElement =  getGrammarTypes().size() - 1;
					}
					switch (getGrammarTypes.get(selectElement)) {
						case "ADJECTIVE" :
							numItems.add(1);
							Pair<String, JsonArray> adjective = this.getGrammarsNP().get(NPgrammarCount).getRandomAdjective();
							System.out.println("Adding adjective: " + adjective.getA());
							newSentenceArray.add(adjective);
							break;
						case "V":
							Pair<String, JsonArray> verb = this.getRandomVerb();
							System.out.println("Adding Verb: " + verb.getA());
							numItems.add(1);
							newSentenceArray.add(verb);
							break;
						default : 
							numItems.add(this.getGrammarsNPPair().get(NPgrammarCount).size());
							for (Pair<String, JsonArray> pair : this.getGrammarsNPPair().get(NPgrammarCount)) {
								System.out.println("Adding this pair: " + pair.getA());
								newSentenceArray.add(pair);
							}
							if (NPgrammarCount < this.getGrammarsNPPair().size() - 1) {
								NPgrammarCount++;
							}
							break;
					}
				}
			}
			System.out.println("END WORKING ON THIS: ");
			System.out.println("restriction: " + restriction);
			int dotPointA = restriction.getA().indexOf(".");
			int dotPointB = restriction.getB().indexOf(".");
			String restrictionType = restriction.getA().substring(dotPointA + 1, restriction.getA().length());
			switch (restrictionType) {
				case "num": 
					String elementA = restriction.getA().substring(0, dotPointA);
					String elementB = restriction.getB().substring(0, dotPointB);
					System.out.println("ElementA: " + elementA);
					System.out.println("ElementA: " + elementB);
					Pair<String, String> pair = new Pair<String, String>(elementA, elementB);
					newSentenceArray = applyNumRestrictions(pair, newSentenceArray, numItems);
					for (Pair<String, JsonArray> finalPair : newSentenceArray) {
						System.out.println("finalPair: " + finalPair.getA());
					}
					break;
			}
			iteration++;
		}
		return newSentenceArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyRestrictions(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		ArrayList<Pair<String, JsonArray>> returnArray = new ArrayList<Pair<String, JsonArray>>();
		for (int i = 0; i < sentenceArray.size(); i++) {
			if (sentenceArray.get(i) != null) {
				System.out.println(sentenceArray.get(i).getA());
				System.out.println(sentenceArray.get(i).getB());
			}
		}
		if (this.emptySentenceArray(sentenceArray)) {
			returnArray.addAll(this.applyRestrictionsSNP(sentenceArray));
		}
		return returnArray;
	}

	public ArrayList<Pair<String, JsonArray>> getVerbs() {
		return verbs;
	}

	public void setVerbs(ArrayList<Pair<String, JsonArray>> verbs) {
		this.verbs = verbs;
	}

	public ArrayList<PrintableObject> getNames() {
		return names;
	}

	public void setNames(ArrayList<PrintableObject> names) {
		this.names = names;
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

