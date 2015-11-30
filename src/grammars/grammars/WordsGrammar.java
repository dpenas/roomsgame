package grammars.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;
import net.slashie.util.Pair;
import util.RandUtil;

public class WordsGrammar {
	
	public static ArrayList<Pair<String, JsonArray>> getAdjectives(JsonObject rootObj, ArrayList<String> adjectives) {
		ArrayList<Pair<String, JsonArray>> itemsReturn = new ArrayList<Pair<String, JsonArray>>();
		JsonObject adjectivesJSON = JSONParsing.getElement((JsonObject)rootObj, "ADJ").getAsJsonObject();
		for (int i = 0; i < adjectives.size(); i++) {
			JsonArray jsonArray = JSONParsing.getElement(adjectivesJSON, adjectives.get(i)).getAsJsonArray();
			String string = adjectives.get(i);
			Pair<String, JsonArray> pair = new Pair<String, JsonArray>(string, jsonArray);
			itemsReturn.add(pair);
		}
		return itemsReturn;
	}
	
	public static Pair<String, JsonArray> getName(JsonObject rootObj, String name) {
		JsonObject nameJSON = JSONParsing.getElement(rootObj, "N").getAsJsonObject();
		JsonArray jsonArray = JSONParsing.getElement(nameJSON, name).getAsJsonArray();
		try {
			jsonArray = JSONParsing.getElement(nameJSON, name).getAsJsonArray();
		} catch(IllegalStateException e){
			System.err.println("ERROR: Word doesn't exist in the the given dictionary: " + e.getMessage());
		}
		return new Pair<String, JsonArray>(name, jsonArray);
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

}
