package translations;

import static org.junit.Assert.*;

import translations.Translations;
import translations.exceptions.WordNotFoundException;
import items.consumables.LifeExtendedPotion;
import items.consumables.LifePotion;
import items.consumables.MagicPotion;
import items.wereables.NormalArmor;
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

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class TranslationsTest {
	public static String language;
	public static String country;
	public static Locale currentLocale;
	public static ResourceBundle messagesWereables;
	
	@Before
	public void setUp() throws IOException, JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException{
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
			LifeExtendedPotion lifePotionExtended = new LifeExtendedPotion(0, 10, "", null, null, null, 
					null, 10);
			assertEquals(lifePotionExtended.getName(), "Extended Life Potion");
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
			NormalArmor normalChestSpanish = new NormalArmor("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			NormalPants normalPantsSpanish = new NormalPants("", 10, 0, 0, null, null, null, 
					null, 0, 1, true);
			LifePotion lifePotion = new LifePotion(null, null, null, null);
			LifeExtendedPotion lifePotionExtended = new LifeExtendedPotion(0, 10, "", null, null, null, 
					null, 10);
			MagicPotion magicPotion = new MagicPotion(null, null, null, null);
			assertEquals(weaponSpanish.getName(), "sword");
			assertEquals(magicPotion.getName(), "potion");
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
