package grammars.grammars;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import characters.active.ActiveCharacter;
import characters.active.enemies.Goblin;
import grammars.parsing.JSONParsing;
import items.Item;
import items.ItemEnumerate;
import items.ItemEnumerate.WeaponType;
import items.wereables.ShortSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import map.Map;
import map.Room;
import util.Tuple;

public class GrammarSelectorSTest {
	
	ActiveCharacter user;
	static JsonParser parser = new JsonParser();
	static JsonObject rootObj;
	static JsonObject rootObjWords;
	static GrammarsGeneral grammarDescribeCharacterWears;
	Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(20, 20);
	Map map = new Map(initial_point, final_point);
	Room room = map.getRooms().get(0);
	Tuple<Integer, Integer> position = room.getRandomInsidePosition();
	
	@Before
	public void setUp() throws IOException, JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException{
		rootObj = parser.parse(new FileReader("./src/grammars/languages/sentenceGrammar" + "EN" + ".json")).getAsJsonObject();
		rootObjWords = parser.parse(new FileReader("./src/grammars/languages/words" + "EN" + ".json")).getAsJsonObject();
		JsonObject objectCharacterWears = JSONParsing.getElement(rootObj, "DESCCHAWEARS").getAsJsonObject();
		grammarDescribeCharacterWears = new GrammarsGeneral(objectCharacterWears);
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("small");
		user = new ActiveCharacter("hero", "", map, room, position, 40, 10,
				100, 100, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, null, adjectives, 0);
		ArrayList<ItemEnumerate.WeaponType> itemTypeWeapon;
		itemTypeWeapon = new ArrayList<ItemEnumerate.WeaponType>();
		itemTypeWeapon.add(WeaponType.LEFTHAND);
		WereableWeapon oneHandSword = new ShortSword(user, null, null, null, 0, true);
		oneHandSword.setWeaponType(itemTypeWeapon);
		user.putItemInventory(oneHandSword);
		user.equipWeapon(oneHandSword);
	}
	
	public static String _getMessage(GrammarIndividual grammarIndividual, ArrayList<PrintableObject> names, String type, boolean usePronoun, boolean useAnd) {
		GrammarSelectorS selector = null;
		try {
			selector = new GrammarSelectorS(grammarIndividual, rootObjWords, names, type, type);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		if (selector != null) {
			if (!usePronoun) {
				return selector.getRandomSentence();
			} else if (useAnd){
				return selector.getRandomSentence(true, true);
			} else {
				return selector.getRandomSentence(true, false);
			}
		}
		
		return "";
	}
	
	public static String generatePrintMessage(ArrayList<PrintableObject> names, GrammarsGeneral grammar, String type, boolean usePronoun,
			boolean useAnd) {
		GrammarIndividual grammarIndividual = grammar.getRandomGrammar();
		return _getMessage(grammarIndividual, names, type, usePronoun, useAnd);
	}
	
	@Test
	public void testGrammarSelectorDescWears() {
		ArrayList<Item> hands = user.getWearHandsAttack();
		String message = "<html>";
		for (Item itemhand : hands) {
			ArrayList<String> preposition = new ArrayList<String>();
			preposition.add("in");
			PrintableObject head = new PrintableObject("hand", "", null, null);
			head.setPrepositions(preposition);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(itemhand);
			names.add(head);
			GrammarIndividual grammarIndividual = grammarDescribeCharacterWears.getRandomGrammar();
			message += _getMessage(grammarIndividual, names, "DESCWEARS", true, false) + "<br>";
		}
		System.out.println(message);
		if (
				message.equals("<html>  he wears the sword in the hand<br>") || 
				message.equals("<html>  he wears the one hand sword in the hand<br>")||
				message.equals("<html>  he wears the short sword in the hand<br>") ||
				message.equals("<html>  he wears the magic sword in the hand<br>")) {
			assertTrue("", true);
		} else {
			assertTrue("", false);
		}
	}
	
	@Test
	public void testGrammarSelectorDescInventory() {
		WereableWeapon oneHandSword = new ShortSword(user, null, null, null, 0, true);
		user.putItemInventory(oneHandSword);
		GrammarsGeneral grammarDescribeItem;
		JsonObject objectDescribeItem = JSONParsing.getElement(rootObj, "DESCITEM").getAsJsonObject();
		grammarDescribeItem = new GrammarsGeneral(objectDescribeItem);
		String message = ""; 
		if (user.getInventory().size() > 0) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			for (Item item : user.getInventory()) {
				names.add(item);
			}
			message += generatePrintMessage(names, grammarDescribeItem, "DESCITEM", false, false);
		}
		System.out.println(message);
		if (message.contains("hero") && message.contains("sword")){
			assertTrue("", true);
		} else {
			assertTrue("", false);
		}
	}
}
