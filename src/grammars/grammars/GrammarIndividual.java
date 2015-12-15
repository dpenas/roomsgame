package grammars.grammars;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonObject;

import net.slashie.util.Pair;

public class GrammarIndividual {
	
	private JsonObject object;
	private ArrayList<Pair<String, String>> restrictions;
	private Map<String, ArrayList<String>> grammar;
	private ArrayList<String> typeWordGrammar;
	private ArrayList<String> numberGrammar;
	
	public GrammarIndividual(JsonObject object) {
		System.out.println("WHAT I RECEIVE");
		System.out.println(object);
		System.out.println("FINISH WHAT I RECEIVE");
		this.object = object;
		this.restrictions = this.setRestrictions(GrammarsRetrieval.getRestrictions(object));
		this.grammar = GrammarsRetrieval.getGrammar(object);
		this.setTypeWordGrammar(this.setTypeGrammar());
		this.setNumberGrammar(this.setNumberGrammar());
	}
	
	public ArrayList<String> setTypeGrammar() {
		ArrayList<String> grammarTypes = new ArrayList<String>();
		for (String grammar : this.getGrammar().get("keys")) {
			System.out.println("HOOOOOOOLAAAAAA: " + grammar);
			int endType = 0;
			if (grammar.equals(grammar.toUpperCase())) {
				endType = grammar.indexOf("_");
				if (endType > -1) {
					grammarTypes.add(grammar.substring(0, endType));
				}
			} else {
				grammarTypes.add(grammar);
			}
		}
		return grammarTypes;
	}
	
	public ArrayList<String> setNumberGrammar() {
		ArrayList<String> grammarNumbers = new ArrayList<String>();
		for (String grammar : this.getGrammar().get("keys")) {
			int beginType = 0;
			if (grammar.equals(grammar.toUpperCase())) {
				beginType = grammar.indexOf("_");
				if (beginType > -1) {
					grammarNumbers.add(grammar.substring(beginType, grammar.length()));
				}
			} else {
				grammarNumbers.add("");
			}
		}
		return grammarNumbers;
	}

	public JsonObject getObject() {
		return object;
	}

	public void setObject(JsonObject object) {
		this.object = object;
	}

	public ArrayList<Pair<String,String>> getRestrictions() {
		return restrictions;
	}

	public ArrayList<Pair<String, String>> setRestrictions(Map<String, ArrayList<String>> restrictions) {
		ArrayList<Pair<String, String>> newRestrictions = new ArrayList<Pair<String, String>>();
		ArrayList<String> keys = restrictions.get("keys");
		ArrayList<String> values = restrictions.get("values");
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = values.get(i);
			Pair<String, String> pair = new Pair<String, String>(key, value);
			newRestrictions.add(pair);
		}
		return newRestrictions;
	}

	public Map<String, ArrayList<String>> getGrammar() {
		return grammar;
	}

	public void setGrammar(Map<String, ArrayList<String>> grammar) {
		this.grammar = grammar;
	}

	public ArrayList<String> getTypeWordGrammar() {
		return typeWordGrammar;
	}

	public void setTypeWordGrammar(ArrayList<String> typeWordGrammar) {
		this.typeWordGrammar = typeWordGrammar;
	}

	public ArrayList<String> getNumberGrammar() {
		return numberGrammar;
	}

	public void setNumberGrammar(ArrayList<String> numberGrammar) {
		this.numberGrammar = numberGrammar;
	}

}
