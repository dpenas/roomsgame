package translations;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
			throws WordNotFoundException, UnsupportedEncodingException{
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
					throw new WordNotFoundException("");
				}
				JsonElement finalAttribute = object.get(attribute);

				if (finalAttribute == null){
					JsonElement originalNameAttribute = object.get("original");
					if (originalNameAttribute != null) {
						return originalNameAttribute.getAsString();
					}
					else {
						return word;
					}
				}
				return finalAttribute.getAsString();
				
			} catch (UnsupportedEncodingException e) {
				return "";
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
	
	public static String spanishNameItem(String name, ArrayList<String> nameAttributes, Locale locale) throws UnsupportedEncodingException{
		String finalName = "";
		finalName = finalName + main.Main.messagesWereables.getString(name);
		String adjectives = "";
		if (finalName.length() == 0){
			finalName = name;
		}
		
		for(String adjective: nameAttributes){
			try {
				adjective = main.Main.messagesWereables.getString(adjective);
				if (getAttributeWordFromJSON(finalName, "gender", "Spanish").equals("f")){
					adjective = getAttributeWordFromJSON(adjective, "feminine", "Spanish");
				}
			} catch (WordNotFoundException e) {
				
			}
			adjectives = adjectives + " " + adjective;
		}
		finalName = finalName + adjectives;
		return finalName;
	}
	
	public static String getNameItem(String name, ArrayList<String> nameAttributes) throws UnsupportedEncodingException{
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
