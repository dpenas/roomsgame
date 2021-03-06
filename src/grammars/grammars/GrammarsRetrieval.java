package grammars.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;

public class GrammarsRetrieval {
	
	private static Map<String, ArrayList<String>> getElementsFromJsonArray(JsonArray element){
		Iterator<JsonElement> iterator = element.iterator();
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		while(iterator.hasNext()) {
			JsonObject object = (JsonObject)iterator.next();
			String key = JSONParsing.getSpecificKeyFromSet(0, object);
			String value = JSONParsing.getSpecificValueFromSet(0, object).getAsString();
			keys.add(key);
			values.add(value);
		}
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		map.put("keys", keys);
		map.put("values", values);
		return map;
	}
	
	public static Map<String, ArrayList<String>> getRestrictions(JsonElement rootObj) {
		if (rootObj.isJsonObject()){
			JsonArray restrictions = (JsonArray) JSONParsing.getElement((JsonObject)rootObj, "restrictions");
			return getElementsFromJsonArray(restrictions);
		}
		return null;
	}
	
	public static Map<String, ArrayList<String>> getGrammar(JsonElement rootObj) {
		if (rootObj.isJsonObject()){
			JsonArray grammar = (JsonArray) JSONParsing.getElement((JsonObject)rootObj, "S");
			return getElementsFromJsonArray(grammar);
		}
		return null;
	}
	
	public static JsonObject getGrammar(JsonObject object, int position) {
		return (JsonObject) JSONParsing.getElement(object, JSONParsing.getSpecificKeyFromSet(position, object));
	}
	
	public static Integer getNumberGrammars(JsonObject object) {
		return object.entrySet().size();
	}
}
