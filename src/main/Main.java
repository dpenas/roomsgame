package main;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import characters.active.ActiveCharacter;
import config.ChangeKeyBinding;
import grammars.grammars.GrammarIndividual;
import grammars.grammars.GrammarSelectorNP;
import grammars.grammars.GrammarSelectorS;
import grammars.grammars.GrammarsGeneral;
import grammars.grammars.GrammarsOperational;
import grammars.grammars.PrintableObject;
import grammars.grammars.WordsGrammar;
import grammars.parsing.JSONParsing;
import items.Item;
import items.wereables.NormalArmor;
import items.wereables.NormalGloves;
import items.wereables.NormalHelmet;
import items.wereables.NormalPants;
import items.wereables.ShortSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import magic.FireRing;
import magic.Spell;
import map.Door;
import map.Map;
import map.Room;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import net.slashie.util.Pair;
import util.JTextAreaWithListener;
import util.RandUtil;
import util.Tuple;


public class Main {
	public static String language = new String("EN");
	public static ResourceBundle messagesWereables, keyBinding;
	public static int countElements;
	public static HashMap<String, Integer> keysMap;
	public static boolean debug = true;
	public static boolean testMode = false;
	public static boolean canUsePronoun = false;
	public static char[] usedSymbols = {'.', 'P', 'G', 'A'};
	static Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	static Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(35, 35);
	static ArrayList<Tuple<Integer, Integer>> portals = new ArrayList<Tuple<Integer, Integer>>(); 
	static Integer[] movementInput;
	static Integer[] inventoryInput;
	static Integer[] pickItemInput;
	static Integer[] attackInput;
	public static Integer[] spellInput;
	static Integer[] descriptionInput;
	static Integer[] descriptionWereableInput;
	public static Integer[] throwItemInput;
	public static Integer[] unequipItemInput;
	static Integer[] changeNumericDescInput;
	static Integer[] changeColorsInput;
	static Integer[] rebindKeysInput;
	static Integer[] arrayColors1 = new Integer[]{12,2,3,11,5,15,7};
	static Integer[] arrayColors2 = new Integer[]{8,4,3,11,15,6,14};
	public static Integer[][] arrayColors = {arrayColors1, arrayColors2};
	public static int selectedColor = 0;
	static Map map;
	static Tuple<Integer, Integer> pos = new Tuple<Integer, Integer>(1,1);
	static Room roomEnemy;
	static Room roomCharacter;
	static DefaultCaret caret;
	static WSwingConsoleInterface j = new WSwingConsoleInterface("RoomsGame");
	static ActiveCharacter user = null;
	static boolean firstTime = true;
	static boolean hasChanged = false;
	static boolean hasMoved = false;
	static char previousPositionChar = '.';
	static char previousPositionChar2 = '.';
	static char deepnessScore = 0;
	static boolean isNumericDescription = false;
	static boolean hasUsedPortal = false;
	static boolean hasEquipedItem = false;
	static boolean hasThrownItem = false;
	static boolean hasUnequipedItem = false;
	static boolean hasPickedItem = false;
	static boolean doMonstersTurn = false;
	public static boolean bindingFinished = false;
	public static boolean unequipPressed = false;
	public static boolean spellsPressed = false;
	public static boolean throwPressed = false;
	static JsonParser parser = new JsonParser();
	static JsonObject rootObj;
	public static JTextAreaWithListener messageLabel = new JTextAreaWithListener(j);
	static int caretPosition = 0;
	static JScrollPane jScrollPane;
	static JFrame window;
	static boolean newMatch = true;
	public static JsonObject rootObjWords;
	public static JsonObject rootObjGrammar;
	static GrammarsGeneral grammarAttack;
	static GrammarsGeneral grammarPickItem;
	static GrammarsGeneral grammarUseItem;
	static GrammarsGeneral grammarDescribeItem;
	static GrammarsGeneral grammarDescribePersonal;
	static GrammarsGeneral grammarDescribeEnvironment;
	static GrammarsGeneral grammarDescribeEnvironmentSimple;
	static GrammarsGeneral grammarDescribeCharacterWears;
	static GrammarsGeneral grammarUnvalidDescription;
	static GrammarsGeneral grammarSimpleDescription;
	static GrammarsGeneral grammarAdjectiveDescription;
	static GrammarsGeneral grammarMissDescription;
	static GrammarsGeneral grammarGeneralDescription;
	static GrammarsGeneral grammarSimpleVerb;
	static GrammarsGeneral grammarGeneralObj;
	
	public static boolean isInputType(Integer[] type, int key) {
		return Arrays.asList(type).contains(key);
	}
	
	public static boolean isTwoKeysInput(int key){
		if (isInputType(unequipItemInput, key) || isInputType(throwItemInput, key) || isInputType(spellInput, key)) {
			return true;
		}
		return false;
	}
	
	public static boolean usePronoun() {
		if (canUsePronoun) {
			if (RandUtil.RandomNumber(0, 2) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static void _setKeyMap() {
		ResourceBundle.clearCache();
		keyBinding = ResourceBundle.getBundle("config.keys");
		Enumeration <String> keys = keyBinding.getKeys();
		keysMap = new HashMap<String, Integer>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = keyBinding.getString(key);
			keysMap.put(key, Integer.parseInt(value));
		}
		_bindKeys();
	}
	
	public static void _setLanguage() {
		FileInputStream in;
		Properties languageProperties = new Properties();
		try {
			in = new FileInputStream("src/config/language.properties");
			languageProperties.load(in);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for(Object value: languageProperties.values()) {
			language = (String)value;
		}
	}
	
	public static void printMessage(String message){
		String previousMessage = messageLabel.getText();
		messageLabel.setText(message + "\n" + previousMessage);
		messageLabel.moveCaretPosition(0);
		messageLabel.requestFocus();
	}
	
	public static void _bindKeys() {
		movementInput = new Integer[] {keysMap.get("left"), keysMap.get("right"), keysMap.get("down"), keysMap.get("up")};
		inventoryInput = new Integer[] {keysMap.get("item1"), keysMap.get("item2"), keysMap.get("item3"), keysMap.get("item4"),
				keysMap.get("item5"), keysMap.get("item6")};
		pickItemInput = new Integer[] {keysMap.get("pickItem")};
		attackInput = new Integer[] {keysMap.get("attack")};
		spellInput = new Integer[] {keysMap.get("spell")};
		descriptionInput = new Integer[] {keysMap.get("descInv"), keysMap.get("descStats"), keysMap.get("descMana"), 
				keysMap.get("descMonster"), keysMap.get("descEnv"), keysMap.get("descWalkablePositions")};
		descriptionWereableInput = new Integer[] {keysMap.get("descHead"), keysMap.get("descHands"), keysMap.get("descChest"),
				keysMap.get("descPants"), keysMap.get("descGloves"), keysMap.get("descWereableItems")};
		throwItemInput = new Integer[] {keysMap.get("throwItem")};
		unequipItemInput = new Integer[] {keysMap.get("unequipItem")};
		changeNumericDescInput = new Integer[] {keysMap.get("changeNumericDesc")};
		changeColorsInput = new Integer[] {keysMap.get("changeColors")};
		rebindKeysInput = new Integer[] {keysMap.get("rebindKeys")};
	}
	
	private static void printUserInformation() {
		user._printInventory(j, rootObjGrammar, rootObjWords);
		user._printLife(rootObjWords, j, 0, map.global_fin().y + 1);
		user._printMana(rootObjWords, j, 1, map.global_fin().y + 1);
		j.print(map.global_fin().y + 1, 2, JSONParsing.getTranslationWord("score", "N", rootObjWords) + ": " + 
				Integer.toString(deepnessScore));
		j.print(map.global_fin().y + 1, 3, JSONParsing.getTranslationWord("level", "N", rootObjWords) + ": " + 
				Integer.toString(user.getLevel()));
		j.print(map.global_fin().y + 1, 4, JSONParsing.getTranslationWord("experience", "N", rootObjWords) + ": " + 
				Integer.toString(user.getExperience()) + "/" + user.getNextLevelExperience());
	}
	
	public static void printEverything(boolean needsToPrintGroundObjects){
		j.cls();
		countElements = 4;
		map.printBorders(j, user);
		map.printInside(j, user);
		map.printItems(j, user);
		map.printMonsters(j, user);
		printUserInformation();
		_printInformationMonsters();
		if (needsToPrintGroundObjects) {
			_printGroundObjects();
		}
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
		j.refresh();
	}
	
	public static void _moveCharacterAction(int i) throws JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException{
		Tuple<Integer, Integer> previousPosition = user.getPosition();
        Tuple<Integer, Integer> newPosition = RandUtil.inputMoveInterpretation(i, Arrays.asList(movementInput), user);
		if (user.move(newPosition)){
			hasMoved = true;
        	user.setVisiblePositions();
        	printEverything(true);
        	previousPositionChar = previousPositionChar2;
        	previousPositionChar2 = j.peekChar(newPosition.y, newPosition.x);
        	if (RandUtil.containsString(usedSymbols, j.peekChar(newPosition.y, newPosition.x))){
            	if (hasChanged){
            		if (debug) {
            			System.out.println(map.getSymbolPosition(previousPosition));
            		}
	            	hasChanged = false;
            	}
        	}
        } else {
        	_messageUnvalid();
        	printEverything(true);
        	hasMoved = false;
        }
		if (user.getRoom().isPortal(user.getPosition())) {
			hasUsedPortal = true;
			newMatch = false;
			gameFlow();
		}
	}
	
	public static void _printInformationMonsters() {
		int count = 0;
		for (ActiveCharacter monster : user.getRoom().getMonstersPosition(user.getPosition())) {
			if (!monster.isDead() && count == 0) {
				countElements += 1;
				j.print(map.global_fin().y + 1, countElements, JSONParsing.getTranslationWord("monsters", "N", rootObjWords) + ": ");
				count++;
			}
			if (!monster.isDead()) {
				countElements += 1;
				monster.printMonstersInformation(rootObjWords, j, map.global_fin().y + 1, countElements);
			}
		}
	}
	
	public static void _printGroundObjects(){
		if (user.getRoom().getItemsPosition(user.getPosition()).size() > 0) {
			countElements += 2;
			j.print(map.global_fin().y + 1, countElements, JSONParsing.getTranslationWord("items", "N", rootObjWords) + ": ");
		}
		for (Item item : user.getRoom().getItemsPosition(user.getPosition())) {
			countElements += 1;
			item.printItemsInformation(j, map.global_fin().y + 1, countElements);
		}
	}
	
	public static void _initialize(){
		if (user == null || newMatch) {
			ArrayList<String> adjectives = new ArrayList<String>();
			adjectives.add("big");
			adjectives.add("brave");
			adjectives.add("glorious");
			user = new ActiveCharacter("hero", "", null, null, null, 
					40, 0, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
					new ArrayList<WereableArmor>(), 100, 100, 0,
					new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, null, adjectives, 1);
			user.setNextLevelExperience();
			WereableWeapon oneHandSword = new ShortSword(user, null, null, null, user.getLevel(), true);
			WereableWeapon oneHandSword2 = new ShortSword(user, null, null, null, user.getLevel(), true);
			NormalHelmet helmet = new NormalHelmet(user, null, null, null, user.getLevel(), true);
			NormalArmor chest = new NormalArmor(user, null, null, null, user.getLevel(), true);
			NormalPants pants = new NormalPants(user, null, null, null, user.getLevel(), true);
			NormalGloves gloves = new NormalGloves(user, null, null, null, user.getLevel(), true);
			user.putItemInventory(oneHandSword);
			user.putItemInventory(oneHandSword2);
			user.putItemInventory(helmet);
			user.putItemInventory(chest);
			user.putItemInventory(pants);
			user.putItemInventory(gloves);
			FireRing fireRing = new FireRing();
			user.addSpell(fireRing);
		}
		newMatch = false;
		_initializeMap();
		_setKeyMap();
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
	}
	
	public static void _initializeMap() {
		int min_rooms = 5;
		map = new Map(initial_point, final_point);
		while (map.getRooms().size() < min_rooms || !map.hasPortals()) {
			map = new Map(initial_point, final_point);
		}
		int number = 0;
		boolean notDone = true;
		while (number <= 0 || notDone) {
			roomCharacter = map.getRandomRoom();
			number = RandUtil.RandomNumber(0, roomCharacter.checkFreePositions().size());
			user.setMap(map);
			user.setRoom(roomCharacter);
			if (number > 0 && roomCharacter.getFreePositions().size() > number) {
				user.setPosition(roomCharacter.getFreePositions().get(number));
				user.setVisiblePositions();
				for (Room room: map.getRooms()) {
					room.putRandomPotions();
					room.generateRandomEnemies(user);
				}
				printEverything(true);
				j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
				j.refresh();
				notDone = false;
			}
		}
	}
	
	public static String _getMessage(GrammarIndividual grammarIndividual, ArrayList<PrintableObject> names, String type, String verbType, boolean usePronoun, boolean useAnd) {
		GrammarSelectorS selector = null;
		try {
			selector = new GrammarSelectorS(grammarIndividual, rootObjWords, names, type, verbType);
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
	
	public static void generatePrintMessage(ArrayList<PrintableObject> names, GrammarsGeneral grammar, String type, String verbType, 
			boolean usePronoun, boolean useAnd) {
		GrammarIndividual grammarIndividual = grammar.getRandomGrammar();
		printMessage(_getMessage(grammarIndividual, names, type, verbType, usePronoun, useAnd));
	}
	
	private static void useAndWithItem(Item item) {
		String message = JSONParsing.getTranslationWord("and", "OTHERS", rootObjWords);
		GrammarIndividual grammarIndividual = grammarGeneralObj.getRandomGrammar();
		GrammarSelectorNP selector = new GrammarSelectorNP(grammarIndividual, rootObjWords, item, "GENERAL");
		printMessage(message + " " + selector.getRandomSentenceTranslated());
	}
	
	public static void _inventoryAction(int i){
		int itemNumber = i % keysMap.get("item1");
		if (itemNumber + 1 <= user.getInventory().size()) {
			Item item = user.getInventory().get(itemNumber);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			user.useItem(item);
			printEverything(false);
			if (item.isWereableItem()) {
				if (hasEquipedItem) {
					useAndWithItem(item);
				} else {
					generatePrintMessage(names, grammarUseItem, "EQUIP", "EQUIP", usePronoun(), false);
					hasEquipedItem = true;
				}
			} else {
				generatePrintMessage(names, grammarUseItem, "USE", "USE", usePronoun(), false);
			}
		}
		if (debug) {
			System.out.println(user.getWeaponsEquipped().size());
		}
		hasChanged = false;
	}
	
	private static void _messageDescriptionInventory() {
		if (user.getInventory().size() > 0) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			for (Item item : user.getInventory()) {
				names.add(item);
			}
			generatePrintMessage(names, grammarDescribeItem, "DESCITEM", "DESCITEM", false, false);
		}
	}
	
	private static void _messageDescriptionDead(ActiveCharacter character, boolean popup) {
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("dead");
		character.setAdjectives(adjectives);
		names.add(character);
		if (popup) {
			GrammarIndividual grammarIndividual = grammarAdjectiveDescription.getRandomGrammar();
			String message = _getMessage(grammarIndividual, names, "DESCTOBE", "DESCTOBE", false, false);
			JLabel label= new JLabel();
			label.setText(message);
			label.requestFocusInWindow();
			JOptionPane.showMessageDialog(null, message, "", JOptionPane.PLAIN_MESSAGE);
		}
		generatePrintMessage(names, grammarAdjectiveDescription, "DESCTOBE", "DESCTOBE", false, false);
	}
	
	private static String _messageDescriptionLife(ActiveCharacter character, boolean usePronoun) {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add(character.getLifeAdjective());
		PrintableObject life = new PrintableObject("life", "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(life);
		GrammarIndividual grammarIndividual = grammarDescribePersonal.getRandomGrammar();
		String message = _getMessage(grammarIndividual, names, "DESCPERSONAL", "DESCPERSONAL", usePronoun, usePronoun);
		if (isNumericDescription) {
			String valueToChange = JSONParsing.getElement(WordsGrammar.getAdjectives(rootObjWords, adjectives).get(0).getB(), "translation");
			message = message.replaceAll(valueToChange, String.valueOf(character.getLife()));
		}
		return message;
	}
	
	private static String _messageDescriptionMana(ActiveCharacter character, boolean usePronoun, boolean useAnd) {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add(character.getManaAdjective());
		PrintableObject mana = new PrintableObject("mana", "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(mana);
		GrammarIndividual grammarIndividual = grammarDescribePersonal.getRandomGrammar();
		String message = _getMessage(grammarIndividual, names, "DESCPERSONAL", "DESCPERSONAL", usePronoun, useAnd);
		if (isNumericDescription) {
			String valueToChange = JSONParsing.getElement(WordsGrammar.getAdjectives(rootObjWords, adjectives).get(0).getB(), "translation");
			message = message.replaceAll(valueToChange, String.valueOf(character.getMagic()));
		}

		return message;
	}
	
	private static String _messageDescriptionStats(ActiveCharacter character, boolean isMonster) {
		String message = "";
		message += _messageDescriptionLife(character, false) + " " + GrammarsOperational.getAndTranslation(rootObjWords);
		message += _messageDescriptionMana(character, true, false) + ".";
		
		PrintableObject level = new PrintableObject("level", "", new ArrayList<String>(), null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(level);
		GrammarIndividual grammarIndividual = grammarDescribeEnvironmentSimple.getRandomGrammar();
		message = message + _getMessage(grammarIndividual, names, "DESCGENERAL", "DESCGENERAL", true, isMonster) + " " + character.getLevel();
		
		if (!isMonster) {
			PrintableObject experience = new PrintableObject("experience", "", new ArrayList<String>(), null);
			ArrayList<PrintableObject> namesExperience = new ArrayList<PrintableObject>();
			namesExperience.add(character);
			namesExperience.add(experience);
			grammarIndividual = grammarDescribeEnvironmentSimple.getRandomGrammar();
			message = GrammarsOperational.getAndTranslation(rootObjWords) + message 
					+ _getMessage(grammarIndividual, namesExperience, "DESCPERSONAL", "DESCPERSONAL", true, true) + " " + user.getExperience();
			JsonObject others = JSONParsing.getElement(rootObjWords, "OTHERS").getAsJsonObject();
			JsonArray outOf = JSONParsing.getElement(others, "out of").getAsJsonArray();
			message += " " + JSONParsing.getElement(outOf, "translation") + " " + user.getNextLevelExperience();
		}
		
		return message;
	}
	
	private static void _messageDescriptionMonster() {
		for (ActiveCharacter monster : map.getMonstersPosition(user)) {
			String message = _messageDescriptionStats(monster, true);
			printMessage(message);
		}
	}
	
	private static String _messageDescriptionEnvironment(PrintableObject object, String directions) {
		ArrayList<String> adjectives = new ArrayList<String>();
		PrintableObject direction = new PrintableObject(directions, "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(object);
		names.add(direction);
		GrammarIndividual grammarIndividual = grammarDescribeEnvironment.getRandomGrammar();
		return _getMessage(grammarIndividual, names, "DESCTOBE", "DESCTOBE", false, false);
	}
	
	private static String _messageSimpleEnvironment(PrintableObject object, String directions) {
		PrintableObject direction = new PrintableObject(directions, "", null, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(object);
		names.add(direction);
		GrammarIndividual grammarIndividual = grammarDescribeEnvironmentSimple.getRandomGrammar();
		return _getMessage(grammarIndividual, names, "DESCTOBE", "DESCTOBE", false, false);
	}
	
	private static void _messageDescriptionWalkablePositions() {
		String message = "";
		JsonObject others = JSONParsing.getElement(rootObjWords, "OTHERS").getAsJsonObject();
		JsonArray reachablePositionsMessage = JSONParsing.getElement(others, "reachable positions").getAsJsonArray();
		String translationreachablePositionsMessage = JSONParsing.getElement(reachablePositionsMessage, "translation");
		message += translationreachablePositionsMessage + ": ";
		int count = 0;
		ArrayList<String> reachablePositions = user.getRoom().printableReachablePositionsCharacter(user);
		JsonObject names = JSONParsing.getElement(rootObjWords, "N").getAsJsonObject();
		JsonArray reachablePositionMessage;
		for (String reachablePosition : reachablePositions) {
			reachablePositionMessage = JSONParsing.getElement(names, reachablePosition).getAsJsonArray();
			message += JSONParsing.getElement(reachablePositionMessage, "translation");
			if (count != reachablePositions.size() - 1) {
				message += ", ";
			}
			count++;
		}
		printMessage(message);
	}
	
	private static String _messageDescriptionCharacterWears(Item item, String usePreposition, String bodyPartString) {
		ArrayList<String> preposition = new ArrayList<String>();
		preposition.add(usePreposition);
		PrintableObject bodyPart = new PrintableObject(bodyPartString, "", null, null);
		bodyPart.setPrepositions(preposition);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(user);
		names.add(item);
		names.add(bodyPart);
		GrammarIndividual grammarIndividual = grammarDescribeCharacterWears.getRandomGrammar();
		return _getMessage(grammarIndividual, names, "DESCWEARS", "DESCWEARS", usePronoun(), false);
	}
	
	private static String _messageDescriptionCharacterWearsHands() {
		String message = "";
		if (user.getWeaponsEquipped().size() > 0) {
			Item weapon = user.getWeaponsEquipped().get(0);
			ArrayList<String> preposition = new ArrayList<String>();
			preposition.add("in");
			PrintableObject hand = new PrintableObject("hand", "", null, null);
			hand.setPrepositions(preposition);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(weapon);
			names.add(hand);
			GrammarIndividual grammarIndividual = grammarDescribeCharacterWears.getRandomGrammar();
			message += _getMessage(grammarIndividual, names, "DESCWEARS", "DESCWEARS", usePronoun(), false);
		}
		
		return message;
	}
	
	private static void _messageUnvalid() {
		String message = "";
		PrintableObject that = new PrintableObject("that", "", null, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(user);
		names.add(that);
		GrammarIndividual grammarIndividual = grammarUnvalidDescription.getRandomGrammar();
		message += _getMessage(grammarIndividual, names, "DESCUNVALID", "DESCUNVALID", usePronoun(), false);
		if (!message.isEmpty()) {
			printMessage(message);
		}
	}
	
	private static void _messageDescriptionEnvironment() {
		String message = "";
		ArrayList<Door> alreadyPrintedDoors = new ArrayList<Door>();
		for (Tuple<Integer, Integer> pos : user.getVisiblePositions()) {
			for (ActiveCharacter enemy : user.getRoom().getMonstersPosition(pos)) {
				if (enemy.isDead()) {
					ArrayList<String> adjectives = new ArrayList<String>();
					adjectives.add("dead");
					enemy.setAdjectives(adjectives);
				}
				if (isNumericDescription) {
					String messagePosition = enemy.getPositionDirectionsWithNumbers(user.getPosition()).getA();
					String messageNumbers = enemy.getPositionDirectionsWithNumbers(user.getPosition()).getB();
					message += _messageDescriptionEnvironment(enemy, messagePosition) + messageNumbers;
				} else {
					message += _messageDescriptionEnvironment(enemy, enemy.getPositionDirections(user.getPosition()));
				}
				message += ". ";
			}
			for (Item item : user.getRoom().getItemsPosition(pos)) {
				if (isNumericDescription) {
					String messagePosition = item.getPositionDirectionsWithNumbers(user.getPosition()).getA();
					String messageNumbers = item.getPositionDirectionsWithNumbers(user.getPosition()).getB();
					message += _messageDescriptionEnvironment(item, messagePosition) + messageNumbers;
				} else {
					message += _messageDescriptionEnvironment(item, item.getPositionDirections(user.getPosition()));
				}
				message += ". ";
			}
			for (Door door : user.getRoom().getDoorsPosition(pos)) {
				Tuple<Integer, Integer> position = door.getPositionRoom(user);
				if (position != null && !alreadyPrintedDoors.contains(door)) {
					PrintableObject doorPrintable = new PrintableObject("door", "", door.getAdjectives(), position);
					if (isNumericDescription) {
						String messagePosition = doorPrintable.getPositionDirectionsWithNumbers(user.getPosition()).getA();
						String messageNumbers = doorPrintable.getPositionDirectionsWithNumbers(user.getPosition()).getB();
						message += _messageDescriptionEnvironment(doorPrintable, messagePosition) + messageNumbers;
					} else {
						message += _messageDescriptionEnvironment(doorPrintable, doorPrintable.getPositionDirections(user.getPosition()));
					}
					alreadyPrintedDoors.add(door);
					message += ". ";
				}
			}
			for (Tuple<Integer, Integer> portal : user.getRoom().getPortalsPosition(pos)) {
				if (portal != null) {
					PrintableObject portablePrintable = new PrintableObject("portal", "", null, portal);
					if (isNumericDescription) {
						String messagePosition = portablePrintable.getPositionDirectionsWithNumbers(user.getPosition()).getA();
						String messageNumbers = portablePrintable.getPositionDirectionsWithNumbers(user.getPosition()).getB();
						message += _messageSimpleEnvironment(portablePrintable, messagePosition) + messageNumbers;
					} else {
						message += _messageSimpleEnvironment(portablePrintable, portablePrintable.getPositionDirections(user.getPosition()));
					}
					message += ". ";
				}
			}
		}
		printMessage(message);
	}
	
	public static void descriptionWereables() {
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(user);
		Item helmet = user.getWearHelmet();
		if (helmet != null) {
			names.add(helmet);
		}
		Item chest = user.getWearChest();
		if (chest != null) {
			names.add(chest);
		}
		Item pants = user.getWearPants();
		if (pants != null) {
			names.add(pants);
		}
		Item gloves = user.getWearGloves();
		if (gloves != null) {
			names.add(gloves);
		}
		ArrayList<Item> hands = user.getWearHandsAttack();
		if (hands.size() > 0) {
			names.add(hands.get(0));
		}
		generatePrintMessage(names, grammarDescribeItem, "DESCITEM", "DESCWEARS", usePronoun(), false);
	}
	
	public static void _descriptionAction(int i){
		if (i == keysMap.get("descInv")) {
			_messageDescriptionInventory();
		}
		if (i == keysMap.get("descStats")) {
			printMessage(_messageDescriptionStats(user, false));
		}
		if (i == keysMap.get("descMonster")) {
			_messageDescriptionMonster();
		}
		if (i == keysMap.get("descEnv")) {
			_messageDescriptionEnvironment();
		}
		if (i == keysMap.get("descWalkablePositions")) {
			_messageDescriptionWalkablePositions();
		}
		if (i == keysMap.get("descWereableItems")) {
			descriptionWereables();
		}
		if (i == keysMap.get("descHead")) {
			Item helmet = user.getWearHelmet();
			if (helmet != null) {
				String message = _messageDescriptionCharacterWears(helmet, "on", "head");
				if (!message.isEmpty()) {
					printMessage(message);
				}
			}
		}
		if (i == keysMap.get("descChest")) {
			Item chest = user.getWearChest();
			if (chest != null) {
				String message = _messageDescriptionCharacterWears(chest, "in", "chest");
				if (!message.isEmpty()) {
					printMessage(message);
				}
			}
		}
		if (i == keysMap.get("descPants")) {
			Item pants = user.getWearPants();
			if (pants != null) {
				String message = _messageDescriptionCharacterWears(pants, "on", "legs");
				if (!message.isEmpty()) {
					printMessage(message);
				}
			}
		}
		if (i == keysMap.get("descGloves")) {
			Item gloves = user.getWearGloves();
			if (gloves != null) {
				String message = _messageDescriptionCharacterWears(gloves, "in", "hands");
				if (!message.isEmpty()) {
					printMessage(message);
				}
			}
		}
		if (i == keysMap.get("descHands")) {
			String message = _messageDescriptionCharacterWearsHands();
			if (message.length() > 10) {
				message += "";
				printMessage(message);
			}
		}
		hasChanged = false;
	}
	
	public static void _pickItemAction(){
		Item item = user.pickItem(user.getPosition(), user.getRoom());
		if (user.getInventory().size() <= user.getMaximumItemsInventory() && item != null ) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			if (hasPickedItem) {
				useAndWithItem(item);
			} else {
				hasPickedItem = true;
				generatePrintMessage(names, grammarPickItem, "PICK", "PICK", usePronoun(), false);
			}
			
			printEverything(true);
			hasChanged = false;
		} else {
			_messageUnvalid();
		}
	}
	
	public static void _attackAction(){
		if (user.getWeaponsEquipped().size() <= 0) {
			PrintableObject weapons = new PrintableObject("weapons", "", null, user.getPosition());
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(weapons);
			generatePrintMessage(names, grammarGeneralDescription, "NOTHAVE", "NOTHAVE", usePronoun(), false);
		} else {
			if (map.getMonstersPosition(user).size() > 0) {
				Pair<Boolean, ActiveCharacter> monster = user.weaponAttack();
				printEverything(true);
				ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
				names.add(user);
				names.add(monster.getB());
				names.add(user.getWeaponsEquipped().get(0));
				GrammarIndividual grammarIndividual = grammarAttack.getRandomGrammar();
				String message = _getMessage(grammarIndividual, names, "ATTACK", "ATTACK", usePronoun(), false);
				if (monster.getA()) {
					if (monster.getB().getLife() <= 0) {
						user.addNewExperience(monster.getB().getExperienceGiven());
						monster.getB().setExperienceGiven(0);
						_messageDescriptionDead(monster.getB(), false);
						hasChanged = true;
					} else {
						if (monster.getB().isHasBeenAttackedByHeroe() && RandUtil.RandomNumber(0, 3) == 1) {
							message += " " + JSONParsing.getRandomWord("OTHERS", "again", rootObjWords);
						} else {
							monster.getB().setHasBeenAttackedByHeroe(true);
						}
						// We only print the message if the enemy is alive
						printMessage(message);
					}
				} else {
					GrammarIndividual grammarIndividualMiss = grammarMissDescription.getRandomGrammar();
					ArrayList<PrintableObject> namesMiss = new ArrayList<PrintableObject>();
					ArrayList<String> preposition = new ArrayList<String>();
					preposition.add("but");
					user.setPrepositions(preposition);
					namesMiss.add(user);
					String messageAgain = "";
					String messageMiss = ", " + _getMessage(grammarIndividualMiss, namesMiss, "MISS", "MISS", true, false);
					if (monster.getB().isHasBeenAttackedByHeroe() && RandUtil.RandomNumber(0, 3) == 1) {
						messageAgain += ", " + JSONParsing.getRandomWord("OTHERS", "again", rootObjWords);
					} else {
						monster.getB().setHasBeenAttackedByHeroe(true);
					}
					printMessage(message + messageAgain + messageMiss);
				}
	    	}
		}
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
	}
	
	public static void _spellAction(int keyPressed){
		int itemNumber = keyPressed % keysMap.get("item1");
		for (ActiveCharacter monsterAffected : user.attackSpell(itemNumber, user)) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(user.getSpells().get(itemNumber));
			names.add(monsterAffected);
			generatePrintMessage(names, grammarAttack, "SPELLS", "SPELLS", usePronoun(), false);
			if (monsterAffected.isDead()) {
				user.addNewExperience(monsterAffected.getExperienceGiven());
				monsterAffected.setExperienceGiven(0);
				_messageDescriptionDead(monsterAffected, false);
			}
		}
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
	}
	
	public static void _throwItem(int keyPressed){
		int itemNumber = keyPressed % keysMap.get("item1");
		if (itemNumber + 1 <= user.getInventory().size()) {
			Item item = user.getInventory().get(itemNumber);
			if (user.throwItem(item)) {
				ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
				names.add(user);
				names.add(item);
				if (hasThrownItem) {
					useAndWithItem(item);
				} else {
					generatePrintMessage(names, grammarPickItem, "THROW", "THROW", usePronoun(), false);
					hasThrownItem = true;
				}
				printEverything(true);
			}
			hasChanged = false;
		}
		printEverything(true);	
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
	}
	
	public static void _unequipItem(Item item){
		if (user.unequipItem(item)) {
			printEverything(true);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			if (hasUnequipedItem) {
				useAndWithItem(item);
			} else {
				generatePrintMessage(names, grammarPickItem, "UNEQUIP", "UNEQUIP", usePronoun(), false);
				hasUnequipedItem = true;
			}
			hasChanged = false;
		} else {
			_messageUnvalid();
		}
		printEverything(true);	
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
	}
	
	public static void describeSpells(){
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		PrintableObject spells = new PrintableObject("spells", "", null, null);
		names.add(spells);
		GrammarIndividual grammarIndividual = grammarSimpleVerb.getRandomGrammar();
		String message = _getMessage(grammarIndividual, names, "DESCGENERAL", "DESCGENERAL", false, false) + ": ";
		
		JsonObject namesWords = JSONParsing.getElement(rootObjWords, "N").getAsJsonObject();
		for (Spell spell : user.getSpells()) {
			JsonArray spellName = JSONParsing.getElement(namesWords, spell.getName()).getAsJsonArray();
			message += JSONParsing.getElement(spellName, "translation") + " ";
		}
		printMessage(message);
		hasChanged = false;
		printEverything(true);	
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
	}
	
	public static void unequipItemAction(int itemCode) {
		hasEquipedItem = false;
    	if (isInputType(descriptionWereableInput, itemCode)) {
    		if (itemCode == keysMap.get("descHead")) {
    			Item helmet = user.getWearHelmet();
    			if (helmet != null) {
    				_unequipItem(helmet);
    			}
    		}
    		if (itemCode == keysMap.get("descChest")) {
    			Item chest = user.getWearChest();
    			if (chest != null) {
    				_unequipItem(chest);
    			}
    		}
    		if (itemCode == keysMap.get("descPants")) {
    			Item pants = user.getWearPants();
    			if (pants != null) {
    				_unequipItem(pants);
    			}
    		}
    		if (itemCode == keysMap.get("descGloves")) {
    			Item gloves = user.getWearGloves();
    			if (gloves != null) {
    				_unequipItem(gloves);
    			}
    		}
    		if (itemCode == keysMap.get("descHands")) {
    			if (user.getWeaponsEquipped().size() > 0) {
    				_unequipItem(user.getWeaponsEquipped().get(0));
    			}
    		}
    		printEverything(false);
    		canUsePronoun = true;
    	}
	}
	
	public static void spellAction(int itemCode) {
		doMonstersTurn = true;
    	if (isInputType(inventoryInput, itemCode)) {
    		_spellAction(itemCode);
    		canUsePronoun = true;
    		printEverything(true);
    		setFlagsToFalse();
    	}
    	canUsePronoun = true;
    	printEverything(true);
	}
	
	public static void throwAction(int itemCode) {
		if (isInputType(inventoryInput, itemCode)) {
    		_throwItem(itemCode);
    		canUsePronoun = true;
    		printEverything(true);
    		hasEquipedItem = false;
    		hasUnequipedItem = false;
    		hasPickedItem = false;
    	}
	}
	
	private static void setFlagsToFalse() {
		hasEquipedItem = false;
		hasUnequipedItem = false;
		hasThrownItem = false;
		hasPickedItem = false;
	}
	
	public static void makeMovement(int i) throws JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException {
		_setKeyMap();
		if (isInputType(movementInput, i)){
        	doMonstersTurn = true;
        	_moveCharacterAction(i);
        	setFlagsToFalse();
        }
        else if (isInputType(inventoryInput, i)) {
        	doMonstersTurn = true;
        	_inventoryAction(i);
        	canUsePronoun = true;
        	printEverything(false);
        	hasUnequipedItem = false;
        	hasThrownItem = false;
        	hasPickedItem = false;
        }
        else if (isInputType(pickItemInput, i)) {
        	doMonstersTurn = true;
        	_pickItemAction();
        	canUsePronoun = true;
        	printEverything(true);
        	hasEquipedItem = false;
        	hasUnequipedItem = false;
        	hasThrownItem = false;
        }
        else if (isInputType(attackInput, i)) {
        	doMonstersTurn = true;
        	_attackAction();
        	canUsePronoun = true;
        	printEverything(true);
        	setFlagsToFalse();
        } 
        else if (isInputType(spellInput, i)) {
        	spellsPressed = true;
        	setFlagsToFalse();
        	messageLabel.requestFocus();
        } else if (isInputType(descriptionInput, i) || isInputType(descriptionWereableInput, i)) {
        	setFlagsToFalse();
        	doMonstersTurn = false;
        	_descriptionAction(i);
        	canUsePronoun = true;
        	printEverything(false);
        } else if (isInputType(throwItemInput, i)) {
        	hasUnequipedItem = false;
        	hasEquipedItem = false;
        	hasPickedItem = false;
        	throwPressed = true;
        	messageLabel.requestFocus();
        } else if (isInputType(changeNumericDescInput, i)) {
        	isNumericDescription = !isNumericDescription;
        } else if (isInputType(changeColorsInput, i)) {
        	if (selectedColor == arrayColors.length - 1) {
        		selectedColor = 0;
        	} else {
        		selectedColor++;
        	}
        	printEverything(true);
        } else if (isInputType(unequipItemInput, i)) {
        	hasEquipedItem = false;
        	hasPickedItem = false;
        	hasThrownItem = false;
        	unequipPressed = true;
        	messageLabel.requestFocus();
        } else if (isInputType(rebindKeysInput, i)){
        	rebindKeys();
        }
	}
	
	public static void gameFlow() throws JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException {
		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable");
		if (deepnessScore == 0){
			_initialize();
		} else {
			_initializeMap();
		}
		if (hasUsedPortal) {
			printEverything(true);
			PrintableObject portal = new PrintableObject("portal", "", null, null);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(portal);
			GrammarIndividual grammarIndividual = grammarGeneralDescription.getRandomGrammar();
			printMessage(_getMessage(grammarIndividual, names, "DESCGOESTHROUGH", "DESCGOESTHROUGH", false, false));
			deepnessScore++;
			hasUsedPortal = false;
		}
		for (;;) {
			for (ActiveCharacter monster : user.getRoom().getMonsters()) {
				monster.setAdjectivesMonster(user);
			}
			user.setAdjectivesUser();
			if (user.getLife() > 0) {
				GrammarIndividual grammarIndividual = grammarAttack.getRandomGrammar();
				if (doMonstersTurn) {
					Pair<Boolean, String> message = user.getRoom().monsterTurn(user, grammarIndividual, rootObjWords);
					printEverything(true);
					if (message.getA() && !message.getB().isEmpty()) {
						printMessage(message.getB());
					} else if (!message.getB().isEmpty()){
						GrammarIndividual grammarIndividualMiss = grammarMissDescription.getRandomGrammar();
						ArrayList<PrintableObject> namesMiss = new ArrayList<PrintableObject>();
						ArrayList<String> preposition = new ArrayList<String>();
						ArrayList<String> prepositionBefore = user.getPrepositions();
						preposition.add("but");
						user.setPrepositions(preposition);
						namesMiss.add(user);
						String messageMiss = ", " + _getMessage(grammarIndividualMiss, namesMiss, "MISS", "MISS", true, false);
						user.setPrepositions(prepositionBefore);
						printMessage(message.getB() + messageMiss);
					}	
				}
				int i = j.inkey().code;
				
				System.out.println("Code" + i);
				makeMovement(i);
				
			}
			else {
				_messageDescriptionDead(user, true);
				try {
					deepnessScore = 0;
					newMatch = true;
					messageLabel.setText("");
					main(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void configureTextArea() {
		float sizeIncrease = 3.0f;
		Font font = messageLabel.getFont();
		float size = font.getSize() + sizeIncrease;
		messageLabel.setEditable(false);
		messageLabel.setFont(font.deriveFont(size));
		window = new JFrame();
		caret = (DefaultCaret)messageLabel.getCaret();
		jScrollPane = new JScrollPane(messageLabel);
		window.add(jScrollPane);
		window.setVisible(true);
		window.setBounds(0, 0, 600, 350);
	}
	
	public static void restartMessage() {
		JLabel message = new JLabel();
		message.setText(JSONParsing.getTranslationWord("restart", "OTHERS", rootObjWords));
		message.requestFocusInWindow();
		JOptionPane.showMessageDialog(null, message, "", JOptionPane.PLAIN_MESSAGE);
		System.exit(1);
	}
	
	public static void rebindKeys() {
		try {
			new ChangeKeyBinding(j);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException {
		_setLanguage();
		configureTextArea();
		j.getTargetFrame().requestFocus();
		rootObj = parser.parse(new FileReader("./src/grammars/languages/sentenceGrammar" + language + ".json")).getAsJsonObject();
		rootObjWords = parser.parse(new FileReader("./src/grammars/languages/words" + language + ".json")).getAsJsonObject();
		rootObjGrammar = parser.parse(new FileReader("./src/grammars/languages/objectGrammar" + language + ".json")).getAsJsonObject();
		grammarAttack = new GrammarsGeneral(JSONParsing.getElement(rootObj, "ATTACK").getAsJsonObject());
		grammarPickItem = new GrammarsGeneral(JSONParsing.getElement(rootObj, "PICK").getAsJsonObject());
		grammarUseItem = new GrammarsGeneral(JSONParsing.getElement(rootObj, "USE").getAsJsonObject());
		grammarDescribeItem = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCITEM").getAsJsonObject());
		grammarDescribePersonal = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCPERSONAL").getAsJsonObject());
		grammarDescribeEnvironment = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCENV").getAsJsonObject());
		grammarDescribeEnvironmentSimple = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCENVSIMPLE").getAsJsonObject());
		grammarDescribeCharacterWears = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCCHAWEARS").getAsJsonObject());
		grammarUnvalidDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCUNVALID").getAsJsonObject());
		grammarSimpleDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCSIMPLE").getAsJsonObject());
		grammarAdjectiveDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCRIPTIONADJECTIVE").getAsJsonObject());
		grammarMissDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "ATTACKMISS").getAsJsonObject());
		grammarGeneralDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "GENERAL").getAsJsonObject());
		grammarSimpleVerb = new GrammarsGeneral(JSONParsing.getElement(rootObj, "SIMPLEVERB").getAsJsonObject());
		grammarGeneralObj = new GrammarsGeneral(JSONParsing.getElement(rootObjGrammar, "GENERAL").getAsJsonObject());
		if (!testMode){
			gameFlow();
		}
		
	}
}