package main;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main {
	public static String language;
	public static String country;
	public static Locale currentLocale;
	public static ResourceBundle messagesWereables;

	public static void main(String[] args) {
		language = new String("en");
		country = new String("US");
		currentLocale = new Locale(language, country);
		messagesWereables = ResourceBundle.getBundle("MessagesWereable", currentLocale);
	}

}
