package translations;

import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import main.Main;

public final class Translations {
	
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
	
	public static String spanishNameItem(String name, ArrayList<String> nameAttributes, Locale locale){
		String finalName = "";
		if (finalName.length() > 0) finalName = finalName + main.Main.messagesWereables.getString(name);
		else finalName = name;
		for(String adjective: nameAttributes){
			finalName = finalName + main.Main.messagesWereables.getString(adjective) + " ";
		}
		return finalName;
	}
	
	public static String getNameItem(String name, ArrayList<String> nameAttributes){
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
