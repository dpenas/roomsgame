package translations;

import static org.junit.Assert.*;
import items.wereables.OneHandSword;

import java.io.IOException;
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
	public void setUp() throws IOException{
		language = new String("en");
		country = new String("US");
		currentLocale = new Locale(language, country);
		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		main.Main.main(null);
	}
	
	@Test
	public void testNames(){
		if (main.Main.currentLocale.toString() == "en_US"){
			OneHandSword weaponEnglish = new OneHandSword("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			assertEquals(weaponEnglish.getName(), "One Hand Magic Sword");
		}
		
		if (main.Main.currentLocale.toString().equals("es_ES")){
			
			OneHandSword weaponSpanish = new OneHandSword("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			assertEquals(weaponSpanish.getName(), "");
		}
	}
	

}
