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
				keysMap.get("descMonster"), keysMap.get("descEnv")};
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
        	printEverything(true);
        	j.print(previousPosition.y, previousPosition.x, user.getSymbolRepresentation(), 12);
        	hasMoved = false;
        }
		if (user.getRoom().isPortal(user.getPosition())) {
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
				System.out.println("count elements is: " + countElements);
				j.print(map.global_fin().y + 1, countElements, "Monsters: ");
				count++;
			}
			if (!monster.isDead()) {
				countElements += 1;
				System.out.println("count elements is: " + countElements);
				monster.printMonstersInformation(j, map.global_fin().y + 1, countElements);
			}
		}
	}
	
	public static void _printGroundObjects(){
		System.out.println("User Position " + "(" + user.getPosition().x + "," + user.getPosition().y + ")");
		if (user.getRoom().getItemsPosition(user.getPosition()).size() > 0) {
			System.out.println("HAY ELEMENTOS. User Position " + "(" + user.getPosition().x + "," + user.getPosition().y + ")");
			countElements += 2;
			System.out.println("count elements item is: " + countElements);
			j.print(map.global_fin().y + 1, countElements, "Items: ");
		}
		for (Item item : user.getRoom().getItemsPosition(user.getPosition())) {
			countElements += 1;
			System.out.println("count elements item is: " + countElements);
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
		user.putItemInventory(oneHandSword);
		FireRing fireball = new FireRing();
		user.addSpell(fireball);
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
	
	public static String _getMessage(GrammarIndividual grammarIndividual, ArrayList<PrintableObject> names, String type, boolean usePronoun) {
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
			} else {
				return selector.getRandomSentence(true, true);
			}
		}
		
		return "";
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
			GrammarIndividual grammarIndividual = grammarUseItem.getRandomGrammar();
			printMessage(_getMessage(grammarIndividual, names, "USE", false));
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
			GrammarIndividual grammarIndividual = grammarDescribeItem.getRandomGrammar();
			printMessage(_getMessage(grammarIndividual, names, "DESCITEM", false));
		}
	}
	
	private static String _messageDescriptionLife(ActiveCharacter character, boolean numerical, boolean usePronoun) {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add(character.getLifeAdjective());
		PrintableObject life = new PrintableObject("life", "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(life);
		GrammarIndividual grammarIndividual = grammarDescribePersonal.getRandomGrammar();
		String message = _getMessage(grammarIndividual, names, "DESCPERSONAL", usePronoun);
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
		String message = _getMessage(grammarIndividual, names, "DESCPERSONAL", usePronoun);
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
		String message = _getMessage(grammarIndividual, names, "DESCGENERAL", false);
		return message;
	}
	
	private static void _messageDescriptionEnvironment() {
		String message = "";
		for (Tuple<Integer, Integer> pos : user.getVisiblePositions()) {
			for (ActiveCharacter enemy : user.getRoom().getMonstersPosition(pos)) {
				message += _messageDescriptionEnvironment(enemy, enemy.getPositionDirections(user.getPosition())) + " ";
			}
			for (Item item : user.getRoom().getItemsPosition(pos)) {
				message += _messageDescriptionEnvironment(item, item.getPositionDirections(user.getPosition())) + " ";
			}
			for (Door door : user.getRoom().getDoorsPosition(pos)) {
				ArrayList<Door> alreadyPrintedDoors = new ArrayList<Door>();
				Tuple<Integer, Integer> position = door.getPositionRoom(user);
				if (position != null && !alreadyPrintedDoors.contains(door)) {
					PrintableObject doorPrintable = new PrintableObject("door", "", door.getAdjectives(), position);
					message += _messageDescriptionEnvironment(doorPrintable, doorPrintable.getPositionDirections(user.getPosition())) + " ";
					alreadyPrintedDoors.add(door);
				}
			}
		}
		printMessage(message);
	}
	
	public static void _descriptionAction(int i){
		if (i == keysMap.get("descInv")) {
			_messageDescriptionInventory();
		}
		if (i == keysMap.get("descLife")) {
			printMessage(_messageDescriptionLife(user, false, false));
		}
		if (i == keysMap.get("descMana")) {
			printMessage(_messageDescriptionMana(user, false, false));
		}
		if (i == keysMap.get("descMonster")) {
			_messageDescriptionMonster(false);
		}
		if (i == keysMap.get("descEnv")) {
			_messageDescriptionEnvironment();
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
			System.out.println("Name the name: " + item.getName());
			GrammarIndividual grammarIndividual = grammarPickItem.getRandomGrammar();
			printMessage(_getMessage(grammarIndividual, names, "PICK", false));
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
			System.out.println("Monster Name: " + monster.getName());
			System.out.println("User Name: " + user.getName());
			System.out.println("Size monster adj: " + names.get(0).getAdjectives().get(0));
			System.out.println("Size user adj: " + names.get(1).getAdjectives().get(0));
			if (monster.getLife() <= 0) {
				hasChanged = true;
			} else {
				// We only print the message if the enemy is alive
				GrammarIndividual grammarIndividual = grammarAttack.getRandomGrammar();
				printMessage(_getMessage(grammarIndividual, names, "ATTACK", false));
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
	
	public static void gameFlow() throws JsonIOException, JsonSyntaxException, InstantiationException, IllegalAccessException {
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
				if (debug) {
					System.out.println("Vida user: " + user.getLife());
				}
				GrammarIndividual grammarIndividual = grammarAttack.getRandomGrammar();
				String sentence = user.getRoom().monsterTurn(user, grammarIndividual, rootObjWords);
				if (!sentence.isEmpty()) {
					printMessage(sentence);
					j.cls();
					printEverything(true);
					j.refresh();
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
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
				
				System.out.println("This is the I!: " + i);
				System.out.println("Is pick Item input: " + isPickItemInput(i));
	            if (isMovementInput(i)){
	            	System.out.println("IT IS MovementInput! :");
	            	_moveCharacterAction(i);
	            }
	            else if (isInventoryInput(i)) {
	            	System.out.println("IT IS InventoryInput! :");
	            	_inventoryAction(i);
	            }
	            else if (isPickItemInput(i)) {
	            	System.out.println("IT IS PickItem! :");
	            	_pickItemAction();
	            }
	            else if (isAttackInput(i)) {
	            	System.out.println("IT IS AttackInput! :");
	            	_attackAction();
	            } 
	            else if (isSpellInput(i)) {
	            	System.out.println("IT IS SpellInput! :");
	            	_spellAction(i);
	            } else if (isDescriptionInput(i)) {
	            	System.out.println("IT IS DescriptionInput! :");
	            	_descriptionAction(i);
	            } else {
	            	printEverything(true);
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	            }
	            
	            if (debug) {
	            	System.out.println("Vida user: " + user.getLife());
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
		grammarAttack = new GrammarsGeneral(objectAttack);
		grammarPickItem = new GrammarsGeneral(objectPickItem);
		grammarUseItem = new GrammarsGeneral(objectUseItem);
		grammarDescribeItem = new GrammarsGeneral(objectDescribeItem);
		grammarDescribePersonal = new GrammarsGeneral(objectDescribePersonal);
		grammarDescribeEnvironment = new GrammarsGeneral(objectDescribeEnvironment);
		if (!testMode){
			gameFlow();
		}
		
	}
}