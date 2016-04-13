package grammars.parsing;

import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import grammars.grammars.GrammarsRetrieval;
import main.Main;
import util.RandUtil;

public class JSONParsing {
	
	public static JsonElement getElement(JsonObject object, String element) {
		return object.get(element);
	}
	
	public static String getElement(JsonArray object, String element) {
		for (int i = 0; i < object.size(); i++) {
			JsonElement result = object.get(i).getAsJsonObject().get(element);
			if (result != null) {
				String value = result.getAsString();
				return value;
			}
		}
		return null;
	}
	
	public static JsonElement getSpecificValueFromSet(int position, JsonObject object) {
		Set<Entry<String, JsonElement>> set = object.entrySet();
		int internalPos = 0;
		for (Entry<String, JsonElement> entry: set) {
			if (position == internalPos) {
				return entry.getValue();
			}
			internalPos++;
		}
		return null;
	}
	
	public static String getSpecificKeyFromSet(int position, JsonObject object) {
		Set<Entry<String, JsonElement>> set = object.entrySet();
		int internalPos = 0;
		for (Entry<String, JsonElement> entry: set) {
			if (position == internalPos) {
				return entry.getKey();
			}
			internalPos++;
		}
		return "";
	}
	
	public static void printSet(JsonObject object) {
		Set<Entry<String, JsonElement>> set = object.entrySet();
		if (Main.debug) {
			for (Entry<String, JsonElement> entry: set) {
			    System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		}
	}
	
	public static String getTranslationWord(String word, String type, JsonObject rootObjWords) {
		if (Main.debug) {
			System.out.println(word);
			System.out.println(type);
			System.out.println(rootObjWords);
		}
		JsonArray elementWord = JSONParsing.getElement(JSONParsing.getElement(rootObjWords, type).getAsJsonObject(), word).getAsJsonArray();
		return JSONParsing.getElement(elementWord, "translation");
	}
	
	public static String getRandomWord(String type, String subtype, JsonObject rootObjWords) {
		JsonObject allTypeElements = JSONParsing.getElement(JSONParsing.getElement(rootObjWords, type).getAsJsonObject(), subtype).getAsJsonObject();
		int position = RandUtil.RandomNumber(0, allTypeElements.entrySet().size() - 1);
		JsonArray name = getSpecificValueFromSet(position, allTypeElements).getAsJsonArray();
		String translation = getElement(name, "translation");
		return translation;
	}
}
