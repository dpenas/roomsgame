package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import characters.active.ActiveCharacter;
import characters.active.enemies.Goblin;
import grammars.grammars.GrammarIndividual;
import grammars.grammars.GrammarSelectorS;
import grammars.grammars.GrammarsGeneral;
import grammars.grammars.PrintableObject;
import grammars.grammars.WordsGrammar;
import grammars.parsing.JSONParsing;
import items.Item;
import items.wereables.NormalArmor;
import items.wereables.NormalGloves;
import items.wereables.NormalHelmet;
import items.wereables.NormalPants;
import items.wereables.OneHandSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import magic.FireRing;
import map.Door;
import map.Map;
import map.Room;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import util.RandUtil;
import util.Tuple;


public class Main {
	public static String language = new String("es");
	public static String country = new String("ES");
	public static Locale currentLocale = new Locale(language, country);
	public static ResourceBundle messagesWereables, keyBinding;
	public static int countElements;
	public static HashMap<String, Integer> keysMap;
	public static boolean debug = false;
	public static boolean testMode = false;
	public static boolean canUsePronoun = false;
	public static char[] usedSymbols = {'.', 'P', 'G', 'A'};
	static Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	static Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(20, 20);
	static ArrayList<Tuple<Integer, Integer>> portals = new ArrayList<Tuple<Integer, Integer>>(); 
	static Integer[] movementInput;
	static Integer[] inventoryInput;
	static Integer[] pickItemInput;
	static Integer[] attackInput;
	static Integer[] spellInput;
	static Integer[] descriptionInput;
	static Integer[] descriptionWereableInput;
	static Integer[] throwItemInput;
	static Integer[] unequipItemInput;
	static Map map;
	static Tuple<Integer, Integer> pos = new Tuple<Integer, Integer>(1,1);
	static Room roomEnemy;
	static Room roomCharacter;
	static WSwingConsoleInterface j = new WSwingConsoleInterface("asdasd");
	static ActiveCharacter user;
	static boolean firstTime = true;
	static boolean hasChanged = false;
	static boolean hasMoved = false;
	static char previousPositionChar = '.';
	static char previousPositionChar2 = '.';
	static char deepnessScore = 0;
	static JsonParser parser = new JsonParser();
	static JsonObject rootObj;
	static JsonObject rootObjWords;
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
	
	public static boolean isMovementInput(int key){
		return Arrays.asList(movementInput).contains(key);
	}
	
	public static boolean isInventoryInput(int key){
		return Arrays.asList(inventoryInput).contains(key);
	}
	
	public static boolean isPickItemInput(int key){
		return Arrays.asList(pickItemInput).contains(key);
	}
	
	public static boolean isAttackInput(int key){
		return Arrays.asList(attackInput).contains(key);
	}
	
	public static boolean isSpellInput(int key){
		return Arrays.asList(spellInput).contains(key);
	}
	
	public static boolean isDescriptionInput(int key){
		return Arrays.asList(descriptionInput).contains(key);
	}
	
	public static boolean isDescriptionWereableInput(int key){
		return Arrays.asList(descriptionWereableInput).contains(key);
	}
	
	public static boolean isThrowItemInput(int key){
		return Arrays.asList(throwItemInput).contains(key);
	}
	
	public static boolean isUnequipItemInput(int key){
		return Arrays.asList(unequipItemInput).contains(key);
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
		keyBinding = ResourceBundle.getBundle("config.keys", currentLocale);
		Enumeration <String> keys = keyBinding.getKeys();
		keysMap = new HashMap<String, Integer>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = keyBinding.getString(key);
			keysMap.put(key, Integer.parseInt(value));
		}
		_bindKeys();
	}
	
	public static void printMessage(String message){
//		try {
//			ChangeKeyBinding.editPropertiesFile(j);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		JLabel messageLabel = new JLabel();
		messageLabel.setText(message);
		messageLabel.requestFocusInWindow();
		JOptionPane.showMessageDialog(null, messageLabel, "", JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void _bindKeys() {
		movementInput = new Integer[] {keysMap.get("left"), keysMap.get("right"), keysMap.get("down"), keysMap.get("up")};
		inventoryInput = new Integer[] {keysMap.get("item1"), keysMap.get("item2"), keysMap.get("item3"), keysMap.get("item4"),
				keysMap.get("item5"), keysMap.get("item6")};
		pickItemInput = new Integer[] {keysMap.get("pickItem")};
		attackInput = new Integer[] {keysMap.get("attack")};
		spellInput = new Integer[] {keysMap.get("spell1"), keysMap.get("spell2")};
		descriptionInput = new Integer[] {keysMap.get("descInv"), keysMap.get("descLife"), keysMap.get("descMana"), 
				keysMap.get("descMonster"), keysMap.get("descEnv"), keysMap.get("descWalkablePositions")};
		descriptionWereableInput = new Integer[] {keysMap.get("descHead"), keysMap.get("descHands"), keysMap.get("descChest"),
				keysMap.get("descPants"), keysMap.get("descGloves")};
		throwItemInput = new Integer[] {keysMap.get("throwItem")};
		unequipItemInput = new Integer[] {keysMap.get("unequipItem")};
	}
	
	public static void printEverything(boolean needsToPrintGroundObjects){
		countElements = 2;
		map.printBorders(j, user);
		map.printInside(j, user);
		map.printItems(j, user);
		map.printMonsters(j, user);
		_printInventoryUser();
		_printLifeUser();
		_printManaUser();
		_printScore();
		_printInformationMonsters();
		if (needsToPrintGroundObjects) {
			System.out.println("I need to paint ground objects");
			_printGroundObjects();
		}
	}
	
	public static void _moveCharacterAction(int i) throws JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException{
		Tuple<Integer, Integer> previousPosition = user.getPosition();
        Tuple<Integer, Integer> newPosition = RandUtil.inputMoveInterpretation(i, Arrays.asList(movementInput), user);
		if (user.move(newPosition)){
			hasMoved = true;
        	user.setVisiblePositions();
        	printEverything(false);
        	previousPositionChar = previousPositionChar2;
        	previousPositionChar2 = j.peekChar(newPosition.y, newPosition.x);
        	if (RandUtil.containsString(usedSymbols, j.peekChar(newPosition.y, newPosition.x))){
        		j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
            	j.print(previousPosition.y, previousPosition.x, previousPositionChar, 12);
            	if (hasChanged){
            		if (debug) {
            			System.out.println(map.getSymbolPosition(previousPosition));
            		}
	            	j.print(previousPosition.y, previousPosition.x, map.getSymbolPosition(previousPosition), 12);
	            	hasChanged = false;
            	}
            	
        	} else{
        		if (firstTime) {
        			j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
	            	j.print(previousPosition.y, previousPosition.x, previousPositionChar, 12);
        			firstTime = false;
        		} else {
        			j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
	            	j.print(previousPosition.y, previousPosition.x, previousPositionChar2, 12);
        			firstTime = true;
        		} 
        	}
        } else {
        	_messageUnvalid();
        	printEverything(true);
        	j.print(previousPosition.y, previousPosition.x, user.getSymbolRepresentation(), 12);
        	hasMoved = false;
        }
		if (user.getRoom().isPortal(user.getPosition())) {
			PrintableObject portal = new PrintableObject("portal", "", null, null);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(portal);
			GrammarIndividual grammarIndividual = grammarSimpleDescription.getRandomGrammar();
			printMessage(_getMessage(grammarIndividual, names, "DESCGOESTHROUGH", false, false));
			deepnessScore++;
			gameFlow();
		}
	}
	
	public static void _printInventoryUser(){
		user.printInventory(user.getInventory(), j, map.global_fin().x + 1, 0);
	}
	
	public static void _printLifeUser(){
		user._printLife(j, 0, map.global_fin().y + 1);
	}
	
	public static void _printManaUser(){
		user._printMana(j, 1, map.global_fin().y + 1);
	}
	
	public static void _printScore(){
		j.print(map.global_fin().y + 1, 2, "Score: " + Integer.toString(deepnessScore));
	}
	
	public static void _printInformationMonsters() {
		int count = 0;
		for (ActiveCharacter monster : user.getRoom().getMonstersPosition(user.getPosition())) {
			// TODO: Change this to translation
			if (!monster.isDead() && count == 0) {
				countElements += 1;
				j.print(map.global_fin().y + 1, countElements, "Monsters: ");
				count++;
			}
			if (!monster.isDead()) {
				countElements += 1;
				monster.printMonstersInformation(j, map.global_fin().y + 1, countElements);
			}
		}
	}
	
	public static void _printGroundObjects(){
		if (user.getRoom().getItemsPosition(user.getPosition()).size() > 0) {
			countElements += 2;
			j.print(map.global_fin().y + 1, countElements, "Items: ");
		}
		for (Item item : user.getRoom().getItemsPosition(user.getPosition())) {
			countElements += 1;
			item.printItemsInformation(j, map.global_fin().y + 1, countElements);
		}
	}
	
	public static void _initialize(){
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("small");
		user = new ActiveCharacter("hero", "", null, null, null, 
				40, 0, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, 0, adjectives);
		WereableWeapon oneHandSword = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		WereableWeapon oneHandSword2 = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		NormalHelmet helmet = new NormalHelmet("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		NormalArmor chest = new NormalArmor("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		NormalPants pants = new NormalPants("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		NormalGloves gloves = new NormalGloves("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		user.putItemInventory(oneHandSword);
		user.putItemInventory(oneHandSword2);
		user.putItemInventory(helmet);
		user.putItemInventory(chest);
		user.putItemInventory(pants);
		user.putItemInventory(gloves);
		FireRing fireRing = new FireRing();
		user.addSpell(fireRing);
		_initializeMap();
		_setKeyMap();
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void _initializeMap() {
		map = new Map(initial_point, final_point);
		roomCharacter = map.getRandomRoom();
		int number = RandUtil.RandomNumber(0, roomCharacter.checkFreePositions().size());
		user.setMap(map);
		user.setRoom(roomCharacter);
		user.setPosition(roomCharacter.getFreePositions().get(number));
		user.setVisiblePositions();
		for (Room room: map.getRooms()) {
			room.putRandomPotions();
			room.putRandomGoblins();
		}
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		j.refresh();
	}
	
	public static void _initializeTest(){
		map = new Map(initial_point, final_point);
		roomEnemy = map.getRandomRoom();
		roomCharacter = map.getRandomRoom();
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("small");
		user = new ActiveCharacter("heroe", "", map, map.obtainRoomByPosition(pos), pos, 
				40, 0, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, 0, adjectives);
		_setKeyMap();
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		WereableWeapon oneHandSword = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		WereableWeapon oneHandSword2 = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		Goblin goblin = new Goblin(map, map.obtainRoomByPosition(pos), pos, adjectives);
		Goblin goblin2 = new Goblin(map, map.obtainRoomByPosition(pos), pos, adjectives);
		goblin.putItemInventory(oneHandSword2);
		goblin.equipWeapon(oneHandSword2);
		map.obtainRoomByPosition(pos).getMonsters().add(goblin);
		map.obtainRoomByPosition(pos).getMonsters().add(goblin2);
		
		ArrayList<Item> inventory = new ArrayList<Item>();
		inventory.add(oneHandSword);
		user.setInventory(inventory);
		user.setLife(80);
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		j.refresh();
	}
	
	public static String _getMessage(GrammarIndividual grammarIndividual, ArrayList<PrintableObject> names, String type, boolean usePronoun, boolean useAnd) {
		GrammarSelectorS selector = null;
		try {
			selector = new GrammarSelectorS(grammarIndividual, rootObjWords, names, type);
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
	
	public static void generatePrintMessage(ArrayList<PrintableObject> names, GrammarsGeneral grammar, String type, boolean usePronoun,
			boolean useAnd) {
		GrammarIndividual grammarIndividual = grammar.getRandomGrammar();
		printMessage(_getMessage(grammarIndividual, names, type, usePronoun, useAnd));
	}
	
	public static void _inventoryAction(int i){
		if (debug) {
    		System.out.println(user.getWeaponsEquipped().size());
    	}
		int itemNumber = i % keysMap.get("item1");
		if (itemNumber + 1 <= user.getInventory().size()) {
			Item item = user.getInventory().get(itemNumber);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			generatePrintMessage(names, grammarUseItem, "USE", usePronoun(), false);
			user.useItem(item);
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
			generatePrintMessage(names, grammarDescribeItem, "DESCITEM", false, false);
		}
	}
	
	private static void _messageDescriptionDead(ActiveCharacter character) {
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("dead");
		character.setAdjectives(adjectives);
		names.add(character);
		generatePrintMessage(names, grammarAdjectiveDescription, "DESCGENERAL", false, false);
	}
	
	private static String _messageDescriptionLife(ActiveCharacter character, boolean numerical, boolean usePronoun) {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add(character.getLifeAdjective());
		PrintableObject life = new PrintableObject("life", "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(life);
		GrammarIndividual grammarIndividual = grammarDescribePersonal.getRandomGrammar();
		String message = _getMessage(grammarIndividual, names, "DESCPERSONAL", usePronoun, usePronoun);
		if (numerical) {
			String valueToChange = JSONParsing.getElement(WordsGrammar.getAdjectives(rootObjWords, adjectives).get(0).getB(), "translation");
			message = message.replaceAll(valueToChange, String.valueOf(character.getLife()));
		}
		return message;
	}
	
	private static String _messageDescriptionMana(ActiveCharacter character, boolean numerical, boolean usePronoun) {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add(character.getManaAdjective());
		PrintableObject mana = new PrintableObject("mana", "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(mana);
		GrammarIndividual grammarIndividual = grammarDescribePersonal.getRandomGrammar();
		String message = _getMessage(grammarIndividual, names, "DESCPERSONAL", usePronoun, usePronoun);
		if (numerical) {
			String valueToChange = JSONParsing.getElement(WordsGrammar.getAdjectives(rootObjWords, adjectives).get(0).getB(), "translation");
			message = message.replaceAll(valueToChange, String.valueOf(character.getMagic()));
		}
		return message;
	}
	
	private static void _messageDescriptionMonster(boolean numerical) {
		for (ActiveCharacter monster : map.getMonstersPosition(user)) {
			String message = ""; 
			message += _messageDescriptionLife(monster, numerical, false);
			message += " " + _messageDescriptionMana(monster, numerical, true);
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
		return _getMessage(grammarIndividual, names, "DESCGENERAL", false, false);
	}
	
	private static String _messageSimpleEnvironment(PrintableObject object, String directions) {
		PrintableObject direction = new PrintableObject(directions, "", null, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(object);
		names.add(direction);
		GrammarIndividual grammarIndividual = grammarDescribeEnvironmentSimple.getRandomGrammar();
		return _getMessage(grammarIndividual, names, "DESCGENERAL", false, false);
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
	
	private static void _messageDescriptionCharacterWears(Item item, String usePreposition, String bodyPartString) {
		ArrayList<String> preposition = new ArrayList<String>();
		preposition.add(usePreposition);
		PrintableObject bodyPart = new PrintableObject(bodyPartString, "", null, null);
		bodyPart.setPrepositions(preposition);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(user);
		names.add(item);
		names.add(bodyPart);
		GrammarIndividual grammarIndividual = grammarDescribeCharacterWears.getRandomGrammar();
		String message = _getMessage(grammarIndividual, names, "DESCWEARS", usePronoun(), false);
		if (!message.isEmpty()) {
			printMessage(message);
		}
	}
	
	private static void _messageDescriptionCharacterWearsHands() {
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
			message += _getMessage(grammarIndividual, names, "DESCWEARS", usePronoun(), false) + "<br>";
		}
		if (message.length() > 10) {
			message += "</html>";
			printMessage(message);
		}
	}
	
	private static void _messageUnvalid() {
		String message = "";
		PrintableObject that = new PrintableObject("that", "", null, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(user);
		names.add(that);
		GrammarIndividual grammarIndividual = grammarUnvalidDescription.getRandomGrammar();
		message += _getMessage(grammarIndividual, names, "DESCUNVALID", usePronoun(), false);
		if (!message.isEmpty()) {
			printMessage(message);
		}
	}
	
	private static void _messageDescriptionEnvironment() {
		String message = "<html>";
		ArrayList<Door> alreadyPrintedDoors = new ArrayList<Door>();
		for (Tuple<Integer, Integer> pos : user.getVisiblePositions()) {
			for (ActiveCharacter enemy : user.getRoom().getMonstersPosition(pos)) {
				message += _messageDescriptionEnvironment(enemy, enemy.getPositionDirections(user.getPosition())) + " ";
			}
			message += "<br>";
			for (Item item : user.getRoom().getItemsPosition(pos)) {
				message += _messageDescriptionEnvironment(item, item.getPositionDirections(user.getPosition())) + " ";
			}
			message += "<br>";
			for (Door door : user.getRoom().getDoorsPosition(pos)) {
				Tuple<Integer, Integer> position = door.getPositionRoom(user);
				if (position != null && !alreadyPrintedDoors.contains(door)) {
					PrintableObject doorPrintable = new PrintableObject("door", "", door.getAdjectives(), position);
					message += _messageDescriptionEnvironment(doorPrintable, doorPrintable.getPositionDirections(user.getPosition())) + " ";
					alreadyPrintedDoors.add(door);
				}
			}
			message += "<br>";
			for (Tuple<Integer, Integer> portal : user.getRoom().getPortalsPosition(pos)) {
				if (portal != null) {
					PrintableObject portablePrintable = new PrintableObject("portal", "", null, portal);
					message += _messageSimpleEnvironment(portablePrintable, portablePrintable.getPositionDirections(user.getPosition())) + " ";
				}
			}
			message += "<br>";
		}
		message = message.replaceAll("(<br>)+", "<br>");
		message += "</html>";
		printMessage(message);
	}
	
	public static void _descriptionAction(int i){
		if (i == keysMap.get("descInv")) {
			_messageDescriptionInventory();
		}
		if (i == keysMap.get("descLife")) {
			printMessage(_messageDescriptionLife(user, usePronoun(), false));
		}
		if (i == keysMap.get("descMana")) {
			printMessage(_messageDescriptionMana(user, usePronoun(), false));
		}
		if (i == keysMap.get("descMonster")) {
			_messageDescriptionMonster(false);
		}
		if (i == keysMap.get("descEnv")) {
			_messageDescriptionEnvironment();
		}
		if (i == keysMap.get("descWalkablePositions")) {
			_messageDescriptionWalkablePositions();
		}
		if (i == keysMap.get("descHead")) {
			Item helmet = user.getWearHelmet();
			if (helmet != null) {
				_messageDescriptionCharacterWears(helmet, "on", "head");
			}
		}
		if (i == keysMap.get("descChest")) {
			Item chest = user.getWearChest();
			if (chest != null) {
				_messageDescriptionCharacterWears(chest, "in", "chest");
			}
		}
		if (i == keysMap.get("descPants")) {
			Item pants = user.getWearPants();
			if (pants != null) {
				_messageDescriptionCharacterWears(pants, "on", "legs");
			}
		}
		if (i == keysMap.get("descGloves")) {
			Item gloves = user.getWearGloves();
			if (gloves != null) {
				_messageDescriptionCharacterWears(gloves, "in", "hands");
			}
		}
		if (i == keysMap.get("descHands")) {
			_messageDescriptionCharacterWearsHands();
		}
		hasChanged = false;
	}
	
	public static void _pickItemAction(){
		for (Item item: user.getRoom().getItemsRoom()){
			System.out.println("The items are: " + item.getName());
		}
		Item item = user.pickItem(user.getPosition(), user.getRoom());
		if (item != null) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			generatePrintMessage(names, grammarPickItem, "PICK", usePronoun(), false);
			hasChanged = false;
    	}
		j.cls();
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		j.refresh();
	}
	
	public static void _attackAction(){
		if (map.getMonstersPosition(user).size() > 0) {
			ActiveCharacter monster = user.weaponAttack();
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(monster);
			names.add(user.getWeaponsEquipped().get(0));
			if (monster.getLife() <= 0) {
				_messageDescriptionDead(monster);
				hasChanged = true;
			} else {
				// We only print the message if the enemy is alive
				generatePrintMessage(names, grammarAttack, "ATTACK", usePronoun(), false);
			}
    	}
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void _spellAction(int keyPressed){
		int itemNumber = keyPressed % keysMap.get("spell1");
		user.attackSpell(itemNumber);
		printEverything(true);	
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void _throwItem(int keyPressed){
		int itemNumber = keyPressed % keysMap.get("item1");
		if (itemNumber + 1 <= user.getInventory().size()) {
			Item item = user.getInventory().get(itemNumber);
			if (user.throwItem(item)) {
				ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
				names.add(user);
				names.add(item);
				generatePrintMessage(names, grammarPickItem, "THROW", usePronoun(), false);
			}
			hasChanged = false;
		}
		printEverything(true);	
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void _unequipItem(Item item){
		//TODO: Working here
		if (user.unequipItem(item)) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			System.out.println("Name the name: " + item.getName());
			generatePrintMessage(names, grammarPickItem, "UNEQUIP", usePronoun(), false);
			hasChanged = false;
		}
		printEverything(true);	
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void gameFlow() throws JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException {
		boolean doMonstersTurn = false;
		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		j.cls();
		if (deepnessScore == 0){
			_initialize();
		} else {
			_initializeMap();
		}
		j.refresh();
		for (;;) {
			if (user.getLife() > 0) {
				GrammarIndividual grammarIndividual = grammarAttack.getRandomGrammar();
				if (doMonstersTurn) {
					String sentence = user.getRoom().monsterTurn(user, grammarIndividual, rootObjWords);
					if (!sentence.isEmpty()) {
						printMessage(sentence);
						j.cls();
						printEverything(true);
						j.refresh();
						j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
					}	
				}
				
				if (hasMoved) {
					printEverything(true);
					j.refresh();
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		            hasMoved = false;
				}
				int i = j.inkey().code;
				System.out.println("Code" + i);
				j.cls();
				
	            if (isMovementInput(i)){
	            	doMonstersTurn = true;
	            	_moveCharacterAction(i);
	            }
	            else if (isInventoryInput(i)) {
	            	doMonstersTurn = true;
	            	_inventoryAction(i);
	            	canUsePronoun = true;
	            }
	            else if (isPickItemInput(i)) {
	            	doMonstersTurn = true;
	            	_pickItemAction();
	            	canUsePronoun = true;
	            }
	            else if (isAttackInput(i)) {
	            	doMonstersTurn = true;
	            	_attackAction();
	            	canUsePronoun = true;
	            } 
	            else if (isSpellInput(i)) {
	            	doMonstersTurn = true;
	            	_spellAction(i);
	            	canUsePronoun = true;
	            } else if (isDescriptionInput(i) || isDescriptionWereableInput(i)) {
	            	doMonstersTurn = false;
	            	_descriptionAction(i);
	            	canUsePronoun = true;
	            } else if (isThrowItemInput(i)) {
	            	int itemCode = j.inkey().code;
	            	if (isInventoryInput(itemCode)) {
	            		_throwItem(itemCode);
	            		canUsePronoun = true;
	            	}
	            } else if (isUnequipItemInput(i)) {
	            	int itemCode = j.inkey().code;
	            	if (isDescriptionWereableInput(itemCode)) {
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
	            			System.out.println("Hello mister");
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
	            		canUsePronoun = true;
	            	}
	            }
	            else {
	            	printEverything(true);
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	            }
	            
	            j.cls();
	            printEverything(true);
				j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
				j.refresh();
			}
			else {
				JLabel message = new JLabel();
				message.setText("You are dead");
				message.requestFocusInWindow();
				JOptionPane.showMessageDialog(null, message, "", JOptionPane.PLAIN_MESSAGE);
				try {
					main(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException {
		rootObj = parser.parse(new FileReader("./src/grammars/english/sentenceGrammar.json")).getAsJsonObject();
		rootObjWords = parser.parse(new FileReader("./src/grammars/english/wordsEnglish.json")).getAsJsonObject();
		JsonObject objectAttack = JSONParsing.getElement(rootObj, "ATTACK").getAsJsonObject();
		JsonObject objectPickItem = JSONParsing.getElement(rootObj, "PICK").getAsJsonObject();
		JsonObject objectUseItem = JSONParsing.getElement(rootObj, "USE").getAsJsonObject();
		JsonObject objectDescribeItem = JSONParsing.getElement(rootObj, "DESCITEM").getAsJsonObject();
		JsonObject objectDescribePersonal = JSONParsing.getElement(rootObj, "DESCPERSONAL").getAsJsonObject();
		JsonObject objectDescribeEnvironment = JSONParsing.getElement(rootObj, "DESCENV").getAsJsonObject();
		JsonObject objectDescribeEnvironmentSimple = JSONParsing.getElement(rootObj, "DESCENVSIMPLE").getAsJsonObject();
		JsonObject objectCharacterWears = JSONParsing.getElement(rootObj, "DESCCHAWEARS").getAsJsonObject();
		JsonObject unvalidDescription = JSONParsing.getElement(rootObj, "DESCUNVALID").getAsJsonObject();
		JsonObject simpleDescription = JSONParsing.getElement(rootObj, "DESCSIMPLE").getAsJsonObject();
		JsonObject adjectiveDescription = JSONParsing.getElement(rootObj, "DESCRIPTIONADJECTIVE").getAsJsonObject();
		grammarAttack = new GrammarsGeneral(objectAttack);
		grammarPickItem = new GrammarsGeneral(objectPickItem);
		grammarUseItem = new GrammarsGeneral(objectUseItem);
		grammarDescribeItem = new GrammarsGeneral(objectDescribeItem);
		grammarDescribePersonal = new GrammarsGeneral(objectDescribePersonal);
		grammarDescribeEnvironment = new GrammarsGeneral(objectDescribeEnvironment);
		grammarDescribeEnvironmentSimple = new GrammarsGeneral(objectDescribeEnvironmentSimple);
		grammarDescribeCharacterWears = new GrammarsGeneral(objectCharacterWears);
		grammarUnvalidDescription = new GrammarsGeneral(unvalidDescription);
		grammarSimpleDescription = new GrammarsGeneral(simpleDescription);
		grammarAdjectiveDescription = new GrammarsGeneral(adjectiveDescription);
		if (!testMode){
			gameFlow();
		}
		
	}
}