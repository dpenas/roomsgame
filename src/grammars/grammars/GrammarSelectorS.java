package grammars.grammars;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import grammars.parsing.JSONParsing;
import main.Main;
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

	public GrammarSelectorS(GrammarIndividual grammar, JsonObject wordsGrammar, ArrayList<PrintableObject> names, String type, String verbsType) throws JsonIOException, JsonSyntaxException, FileNotFoundException, InstantiationException, IllegalAccessException {
		super(grammar, wordsGrammar);
		this.type = type;
		this.verbs = WordsGrammar.getVerbs(wordsGrammar, verbsType);
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
		JsonObject rootObj = null;
		rootObj = JSONParsing.getElement(main.Main.rootObjGrammar, type).getAsJsonObject();
		GrammarsGeneral grammarGeneral = new GrammarsGeneral(rootObj);
		this.getGrammarsNP().add(new GrammarSelectorNP(grammarGeneral.getRandomGrammar(), this.getWordsGrammar(), this.getNames().get(namePos), type));
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
		for (int i = 0; i < endIteration; i++) {
			int selectedTypeWordGrammar = i;
			if (selectedTypeWordGrammar >= this.getGrammar().getTypeWordGrammar().size()) {
				selectedTypeWordGrammar = this.getGrammar().getTypeWordGrammar().size() - 1;
			}
			switch (this.getGrammar().getTypeWordGrammar().get(selectedTypeWordGrammar)) {
				case "V" : 
					resultArray.add(getRandomVerb());
					break;
				default: resultArray.add(null);
			}
		}
		if (Main.debug) {
			for (int j = 0; j < resultArray.size(); j++) {
				System.out.println(resultArray.get(j));
			}
		}
		return resultArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> changeValue(ArrayList<Pair<String, JsonArray>> sentenceArray, String valueToChange, 
			String changeToValue, String typeChangeToValue){
		if (Main.debug) {
			System.out.println("Value to change: " + valueToChange);
			System.out.println("Change to: " + changeToValue);
			System.out.println("TypeChangeToValue: " + typeChangeToValue);
		}
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
					if (sentenceArray.get(j).getA().equals(valueToChange)) {
						sentenceArray.set(j, newPair);
					}
				}
			}
		}
		if (Main.debug) {
			for (Pair<String,JsonArray> a : sentenceArray) {
				System.out.println("Sentence Array a: " + a.getA());
				System.out.println("Sentence Array b: " + a.getB());
			}
		}
		return sentenceArray;
	}
	
	private boolean containsArray(String element, String[] array) {
		for (String individualElement : array) {
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
				if (!this.containsArray(completeGrammars.get(i), this.typesNPGrammarComplete)) {
					valuesIndefinite -= this.getGrammarsNPPair().get(numNP).size();
				}
				numNP++;
			} else {
				valuesIndefinite -= 1;
			}
		}
		if (Main.debug) {
			System.out.println("ValuesIndefinite " + valuesIndefinite);
		}
		return valuesIndefinite;
	}
	
	public String getRandomSentence(boolean usePronoun, boolean useAnd) {
		try {
			this.analyseGrammar();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		String sentence = "";
		if (Main.debug) {
			for (int j = 0; j < this.getGrammarsNP().size(); j++) {
				System.out.println(this.getGrammarsNP().get(j));
			}
			for (int j = 0; j < this.getGrammarsNPPair().size(); j++) {
				System.out.println(this.getGrammarsNPPair().get(j));
			}
		}
		
		ArrayList<String> grammarsTotal = this.getGrammar().getGrammar().get("keys");
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		ArrayList<Pair<String, JsonArray>> sentenceArray = this.fillWords();
		sentenceArray = this.applyRestrictions(sentenceArray);
		int grammarNP = 0;
		int iniIndefiniteNP = 0;
		for (int i = 0; i < grammarsTotal.size(); i++) {
			if (this.getGrammarsNPPair().size() - 1 >= grammarNP) {
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
		}
		boolean changed = false;
		for(int i = 0; i < sentenceArray.size(); i++) {
			Pair<String, JsonArray> pair = sentenceArray.get(i);
			if (values.contains(i) && (values.get(values.size() - 2)) == i) {
				String translationAnd = GrammarsOperational.getAndTranslation(this.getWordsGrammar());
				sentence += " " + translationAnd + " " + JSONParsing.getElement(pair.getB(), "translation");
				changed = false;
			} else {
				if (values.contains(i)) {
					sentence += ", " + JSONParsing.getElement(pair.getB(), "translation") + " ";
					changed = true;
				} else {
					if (changed) {
						sentence += JSONParsing.getElement(pair.getB(), "translation");
					} else {
						sentence += " " + JSONParsing.getElement(pair.getB(), "translation");
					}
					changed = false;
				}
			}
		}
		if (usePronoun) {
			String nameToGetPronounFrom = this.getGrammarsNP().get(0).getName().getName();
			String toChangeFor = "";
			String pronoun = JSONParsing.getElement(WordsGrammar.getName(this.getWordsGrammar(), nameToGetPronounFrom).get(0).getB(), "pronoun");
			if (useAnd) {
				String translationAnd = GrammarsOperational.getAndTranslation(this.getWordsGrammar());
				toChangeFor += translationAnd;
			}
			toChangeFor += " " + pronoun + " ";
			String NPToDelete = "";
			for (Pair<String, JsonArray> word : this.getGrammarsNPPair().get(0)) {
				if (!this.getGrammarsNP().get(0).isPreposition(word.getA())) {
					if (Main.debug) {
						System.out.println("Word: " + word.getA());
					}
					NPToDelete += JSONParsing.getElement(word.getB(), "translation") + " ";
				}
			}
			if (Main.debug) {
				System.out.println("NPToDelete: " + NPToDelete);
			}
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
							if (Main.debug) {
								System.out.println("Adding adjective: " + adjective.getA());
							}
							newSentenceArray.add(adjective);
							break;
						case "V":
							Pair<String, JsonArray> verb = this.getRandomVerb();
							if (Main.debug) {
								System.out.println("Adding Verb: " + verb.getA());
							}
							numItems.add(1);
							newSentenceArray.add(verb);
							break;
						default : 
							numItems.add(this.getGrammarsNPPair().get(NPgrammarCount).size());
							for (Pair<String, JsonArray> pair : this.getGrammarsNPPair().get(NPgrammarCount)) {
								newSentenceArray.add(pair);
							}
							if (NPgrammarCount < this.getGrammarsNPPair().size() - 1) {
								NPgrammarCount++;
							}
							break;
					}
				}
			}
			int dotPointA = restriction.getA().indexOf(".");
			int dotPointB = restriction.getB().indexOf(".");
			String restrictionType = restriction.getA().substring(dotPointA + 1, restriction.getA().length());
			String elementA = "";
			String elementB = "";
			Pair<String, String> pair = null;
			switch (restrictionType) {
				case "num": 
					elementA = restriction.getA().substring(0, dotPointA);
					elementB = restriction.getB().substring(0, dotPointB);
					if (Main.debug) {
						System.out.println("ElementA: " + elementA);
						System.out.println("ElementB: " + elementB);
					}
					pair = new Pair<String, String>(elementA, elementB);
					newSentenceArray = applyRestrictions(pair, newSentenceArray, numItems, "num");
					if (Main.debug) {
						for (Pair<String, JsonArray> finalPair : newSentenceArray) {
							System.out.println("finalPair: " + finalPair.getA());
						}
					}
					break;
				case "gen": 
					elementA = restriction.getA().substring(0, dotPointA);
					elementB = restriction.getB().substring(0, dotPointB);
					if (Main.debug) {
						System.out.println("ElementA: " + elementA);
						System.out.println("ElementB: " + elementB);
					}
					pair = new Pair<String, String>(elementA, elementB);
					newSentenceArray = applyRestrictions(pair, newSentenceArray, numItems, "gen");
					if (Main.debug) {
						for (Pair<String, JsonArray> finalPair : newSentenceArray) {
							System.out.println("finalPair: " + finalPair.getA());
						}
					}
					break;
			}
			iteration++;
		}
		return newSentenceArray;
	}
	
	protected ArrayList<Pair<String, JsonArray>> applyRestrictions(ArrayList<Pair<String, JsonArray>> sentenceArray) {
		ArrayList<Pair<String, JsonArray>> returnArray = new ArrayList<Pair<String, JsonArray>>();
		if (Main.debug) {
			for (int i = 0; i < sentenceArray.size(); i++) {
				if (sentenceArray.get(i) != null) {
					System.out.println(sentenceArray.get(i).getA());
					System.out.println(sentenceArray.get(i).getB());
				}
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
	
}

