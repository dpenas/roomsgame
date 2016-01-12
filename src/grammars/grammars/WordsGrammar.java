package grammars.grammars;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import net.slashie.util.Pair;

public class WordsGrammar {
	
	public static ArrayList<Pair<String, JsonArray>> getAdjectives(JsonObject rootObj, ArrayList<String> adjectives) {
		ArrayList<Pair<String, JsonArray>> itemsReturn = new ArrayList<Pair<String, JsonArray>>();
		JsonObject adjectivesJSON = JSONParsing.getElement((JsonObject)rootObj, "ADJ").getAsJsonObject();
		if (adjectives != null) {
			for (int i = 0; i < adjectives.size(); i++) {
				JsonArray jsonArray = JSONParsing.getElement(adjectivesJSON, adjectives.get(i)).getAsJsonArray();
				String string = adjectives.get(i);
				Pair<String, JsonArray> pair = new Pair<String, JsonArray>(string, jsonArray);
				itemsReturn.add(pair);
			}
		}
		return itemsReturn;
	}
	
	public static ArrayList<Pair<String, JsonArray>> getAllAdjectives(JsonObject rootObj) {
		ArrayList<Pair<String, JsonArray>> itemsReturn = new ArrayList<Pair<String, JsonArray>>();
		JsonObject adjectivesJSON = JSONParsing.getElement((JsonObject)rootObj, "ADJ").getAsJsonObject();
		for (int i = 0; i < adjectivesJSON.entrySet().size(); i++) {
			String name = JSONParsing.getSpecificKeyFromSet(i, adjectivesJSON);
			JsonArray value = JSONParsing.getSpecificValueFromSet(i, adjectivesJSON).getAsJsonArray();
			itemsReturn.add(new Pair<String, JsonArray>(name, value));
		}
		return itemsReturn;
	}
	
	public static ArrayList<Pair<String, JsonArray>> getName(JsonObject rootObj, String name) {
		// TODO: Change to search for all the names
		ArrayList<Pair<String, JsonArray>> result = new ArrayList<Pair<String, JsonArray>>();
		JsonObject nameJSON = JSONParsing.getElement(rootObj, "N").getAsJsonObject();
		System.out.println("nameJSON: " + nameJSON);
		System.out.println("name: " + name);
		JsonArray jsonArray = JSONParsing.getElement(nameJSON, name).getAsJsonArray();
		try {
			jsonArray = JSONParsing.getElement(nameJSON, name).getAsJsonArray();
		} catch(IllegalStateException e){
			System.err.println("ERROR: Word doesn't exist in the the given dictionary: " + e.getMessage());
		}
		result.add(new Pair<String, JsonArray>(name, jsonArray));
		return result;
	}
	
	public static ArrayList<Pair<String, JsonArray>> getDeterminant(JsonObject object) {
		ArrayList<Pair<String, JsonArray>> itemsReturn = new ArrayList<Pair<String, JsonArray>>();
		JsonObject determinantsJSON = JSONParsing.getElement((JsonObject)object, "DET").getAsJsonObject();
		for (int i = 0; i < determinantsJSON.entrySet().size(); i++) {
			String name = JSONParsing.getSpecificKeyFromSet(i, determinantsJSON);
			JsonArray value = JSONParsing.getSpecificValueFromSet(i, determinantsJSON).getAsJsonArray();
			itemsReturn.add(new Pair<String, JsonArray>(name, value));
		}
		
		return itemsReturn;
	}
	
	public static ArrayList<Pair<String, JsonArray>> getVerbs(JsonObject object, String type) {
		ArrayList<Pair<String, JsonArray>> itemsReturn = new ArrayList<Pair<String, JsonArray>>();
		JsonObject allVerbsJSON = JSONParsing.getElement((JsonObject)object, "V").getAsJsonObject();
		JsonObject verbsJSON = JSONParsing.getElement(allVerbsJSON, type).getAsJsonObject();
		for (int i = 0; i < verbsJSON.entrySet().size(); i++) {
			String name = JSONParsing.getSpecificKeyFromSet(i, verbsJSON);
			JsonArray value = JSONParsing.getSpecificValueFromSet(i, verbsJSON).getAsJsonArray();
			itemsReturn.add(new Pair<String, JsonArray>(name, value));
		}
		
		return itemsReturn;
	}

}
