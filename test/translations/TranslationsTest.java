package translations;

import static org.junit.Assert.*;
import items.wereables.OneHandSword;
import translations.*;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

public class TranslationsTest {
	public static String language;
	public static String country;
	public static Locale currentLocale;
	public static ResourceBundle messagesWereables;
	
	@Before
	public void setUp(){
		language = new String("en");
		country = new String("US");
		currentLocale = new Locale(language, country);
		messagesWereables = ResourceBundle.getBundle("translations.MessagesWereable", currentLocale);
	}
	
	@Test
	public void test(){
		OneHandSword normalWeapon = new OneHandSword(messagesWereables.getString("normalWeapon"), 
				"", "", 10, 0, 0, null, null, null, null, null, 0, 0, true, 0, 1);
		assertEquals(normalWeapon.getName(), "Normal weapon");
	}
	

}
