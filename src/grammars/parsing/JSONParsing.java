package grammars.parsing;

import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSONParsing {
	
	public static JsonElement getElement(JsonObject object, String element) {
		return object.get(element);
	}
	
	public static String getElement(JsonArray object, String element) {
		for (int i = 0; i < object.size(); i++) {
			String value = object.get(i).getAsJsonObject().get(element).getAsString();
			if (value != null) {
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
		for (Entry<String, JsonElement> entry: set) {
		    System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
}
