package grammars.grammars;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import grammars.parsing.JSONParsing;

public class GrammarsRetrieval {
	
	public static void getRestrictions(JsonElement rootObj) {
		if (rootObj.isJsonObject()){
			JsonArray restrictions = (JsonArray) JSONParsing.getElement((JsonObject)rootObj, "restrictions");
			System.out.println(restrictions.get(0));
		}
	}
}
