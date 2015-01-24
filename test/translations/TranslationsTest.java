package translations;

import static org.junit.Assert.*;
import translations.Translations;
import translations.exceptions.WordNotFoundException;
import items.wereables.NormalChest;
import items.wereables.NormalGloves;
import items.wereables.NormalHelmet;
import items.wereables.NormalPants;
import items.wereables.OneHandSword;
import items.wereables.SmallShield;
import items.wereables.TwoHandSword;

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
	public void testNames() {
		if (main.Main.currentLocale.toString().equals("en_US")){
			OneHandSword weaponEnglish = new OneHandSword("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			assertEquals(weaponEnglish.getName(), "One Hand Magic Sword");
		}
		
		if (main.Main.currentLocale.toString().equals("es_ES")){
			
			OneHandSword weaponSpanish = new OneHandSword("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			TwoHandSword twoWeaponEnglish = new TwoHandSword("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			SmallShield smallShieldSpanish = new SmallShield("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			NormalHelmet normalHelmetShieldSpanish = new NormalHelmet("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			NormalGloves normalGlovesSpanish = new NormalGloves("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			NormalChest normalChestSpanish = new NormalChest("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			NormalPants normalPantsSpanish = new NormalPants("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			assertEquals(weaponSpanish.getName(), "Espada Una Mano Mágica");
			assertEquals(twoWeaponEnglish.getName(), "Espada Dos Manos Mágica");
			assertEquals(smallShieldSpanish.getName(), "Escudo Pequeño Mágico");
			assertEquals(normalHelmetShieldSpanish.getName(), "Casco Normal Mágico");
			assertEquals(normalChestSpanish.getName(), "Pecho Normal Mágico");
			assertEquals(normalGlovesSpanish.getName(), "Guantes Normales Mágicos");
			assertEquals(normalPantsSpanish.getName(), "Pantalones Normales Mágicos");
		}
	}
	
	@Test
	public void attributeFromJSON() {
		String attribute = "";
		try {
			attribute = Translations.getAttributeWordFromJSON("Espada", "gender", "Spanish");
		} catch (WordNotFoundException e) {
			
		}
		assertEquals(attribute, "f");
	}
	
}
