package translations;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;

import translations.exceptions.WordNotFoundException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.Main;

public final class Translations {
	
	public static String translationsURL = "/translations/files/";
	
	public static String getAttributeWordFromJSON(String word, String attribute, String language) 
			throws WordNotFoundException {
		Locale locale = Main.currentLocale;
		String localeString = getStringLocale(locale);
		if (localeString.length() > 0){
			String fileName = language + "Dictionary.json";
			String finalPath = translationsURL + fileName;
			try (Reader reader = new InputStreamReader
					(Main.class.getResourceAsStream(finalPath), "UTF-8")){
				JsonParser parser = new JsonParser();
				JsonObject rootObj = parser.parse(reader).getAsJsonObject();
				JsonObject object = rootObj.getAsJsonObject(word);
				if (object == null){
					if (Main.debug){
						System.out.println("There is a word missing in the translation file");
						throw new WordNotFoundException("The word is not in the file");
					}
					return word;
				}
				JsonElement finalAttribute = object.get(attribute);

				if (finalAttribute == null){
					JsonElement originalNameAttribute = object.get("original");
					if (originalNameAttribute != null) {
						return originalNameAttribute.getAsString();
					}
					else {
						System.out.println("The attribute of " + attribute + "is missing");
						return word;
					}
				}
				return finalAttribute.getAsString();
				
			} catch (IOException e) {
				return "";
			}
		}
		return "";
	}
	
	public static boolean validateLocale(Locale locale){
		try {
			return locale.getISO3Language() != null && locale.getISO3Country() != null;
		} catch (MissingResourceException e){
			return false;
		}
	}
	
	public static String getStringLocale(Locale locale){
		if (validateLocale(locale)){
			return locale.toString();
		}
		return "";
	}
	
	public static String englishNameItem(String name, ArrayList<String> nameAttributes, Locale locale){
		String finalName = "";
		for(String adjective: nameAttributes){
			finalName = finalName + main.Main.messagesWereables.getString(adjective) + " ";
		}
		if (finalName.length() > 0) finalName = finalName + main.Main.messagesWereables.getString(name);
		else finalName = name;
		return finalName;
	}
	
	public static String spanishNameItem(String name, ArrayList<String> nameAttributes, Locale locale) {
		String finalName = "";
		finalName = finalName + main.Main.messagesWereables.getString(name);
		String adjectives = "";
		boolean isChanged = false;
		boolean isPlural = false;
		if (finalName.length() == 0){
			finalName = name;
		}
		
		for(String adjective: nameAttributes){
			isChanged = false;
			try {
				adjective = main.Main.messagesWereables.getString(adjective);
				//System.out.println(adjective);
				if (adjective.equals("Pocion")){
					System.out.println("HOLA");
				}
				if (getAttributeWordFromJSON(adjective, "type", "Spanish").equals("adjective")){
					if (getAttributeWordFromJSON(adjective, "isPlural", "Spanish").equals("y")){
						isPlural = true;
					}
				}
				if (getAttributeWordFromJSON(finalName, "gender", "Spanish").equals("f")){
					if (getAttributeWordFromJSON(finalName, "isPlural", "Spanish").equals("y")){
						adjective = getAttributeWordFromJSON(adjective, "pluralfeminine", "Spanish");
					}
					adjective = getAttributeWordFromJSON(adjective, "feminine", "Spanish");
					isChanged = true;
				}
				if (isPlural && getAttributeWordFromJSON(adjective, "type", "Spanish").equals("name")){
					adjective = getAttributeWordFromJSON(adjective, "plural", "Spanish");
					isPlural = false;
					isChanged = true;
				}
				if (getAttributeWordFromJSON(finalName, "isPlural", "Spanish").equals("y")){
					adjective = getAttributeWordFromJSON(adjective, "plural", "Spanish");
					isChanged = true;
				}
				if (!isChanged){
					adjective = getAttributeWordFromJSON(adjective, "original", "Spanish");
				}
			} catch (WordNotFoundException e) {
				
			}
			
			adjectives = adjectives + " " + adjective;
		}
		try {
			System.out.println(finalName);
			if (finalName.equals(main.Main.messagesWereables.getString(name))){
				System.out.println(finalName);
				finalName = getAttributeWordFromJSON(finalName, "original", "Spanish");
			}
		} catch (WordNotFoundException e) {
			if (Main.debug){
				System.out.println("Word not Found!!");
			}
		}
		finalName = finalName + adjectives;
		return finalName;
	}
	
	public static String getNameItem(String name, ArrayList<String> nameAttributes) {
		Locale locale = Main.currentLocale;
		String localeString = getStringLocale(locale);
		String finalName = "";
		if (localeString.length() > 0){
			switch (localeString){
				case "en_US": finalName = englishNameItem(name, nameAttributes, locale);
					break;
				case "es_ES": finalName = spanishNameItem(name, nameAttributes, locale);
					break;
				default:
					break;
			}
			return finalName;
		}
		return name;
	}

}
