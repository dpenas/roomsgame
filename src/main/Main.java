package main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main {
	public static String language = new String("es");
	public static String country = new String("ES");
	public static Locale currentLocale = new Locale(language, country);
	public static ResourceBundle messagesWereables;

	public static void main(String[] args) throws IOException {
		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		
	}
}
