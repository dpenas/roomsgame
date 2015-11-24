package grammars.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;

public class GrammarsRetrieval {
	
	public static Map<String, ArrayList<String>> getRestrictions(JsonElement rootObj) {
		if (rootObj.isJsonObject()){
			JsonArray restrictions = (JsonArray) JSONParsing.getElement((JsonObject)rootObj, "restrictions");
			Iterator iterator = restrictions.iterator();
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			while(iterator.hasNext()) {
				JsonObject element = (JsonObject)iterator.next();
				String key = JSONParsing.getSpecificKeyFromSet(0, element);
				String value = JSONParsing.getSpecificValueFromSet(0, element).getAsString();
				keys.add(key);
				values.add(value);
			}
			Map<String, ArrayList<String>> map = new HashMap();
			map.put("keys", keys);
			map.put("values", values);
			return map;
		}
		return null;
	}
}
