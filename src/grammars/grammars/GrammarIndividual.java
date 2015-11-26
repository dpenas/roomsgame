package grammars.grammars;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GrammarIndividual {
	
	private JsonObject object;
	private Map<String, ArrayList<String>> restrictions;
	private Map<String, ArrayList<String>> grammar;
	
	public GrammarIndividual(JsonObject object) {
		this.object = object;
		this.restrictions = GrammarsRetrieval.getRestrictions(object);
		this.grammar = GrammarsRetrieval.getGrammar(object);
	}

	public JsonObject getObject() {
		return object;
	}

	public void setObject(JsonObject object) {
		this.object = object;
	}

	public Map<String, ArrayList<String>> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(Map<String, ArrayList<String>> restrictions) {
		this.restrictions = restrictions;
	}

	public Map<String, ArrayList<String>> getGrammar() {
		return grammar;
	}

	public void setGrammar(Map<String, ArrayList<String>> grammar) {
		this.grammar = grammar;
	}

}
