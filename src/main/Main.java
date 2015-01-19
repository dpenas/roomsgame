package main;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Main {
	public static String language = new String("es");
	public static String country = new String("ES");
	public static Locale currentLocale = new Locale(language, country);
	public static ResourceBundle messagesWereables;

	public static void main(String[] args) throws IOException {
		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		try (Reader reader = new InputStreamReader
				(Main.class.getResourceAsStream("/translations/files/SpanishDictionary.json"), "UTF-8")){
			JsonParser parser = new JsonParser();
			JsonObject rootObj = parser.parse(reader).getAsJsonObject();
			JsonObject espada = rootObj.getAsJsonObject("espada");
			System.out.println(espada.get("gender").getAsString());
			
		}
	}

}
