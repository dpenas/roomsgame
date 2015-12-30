package main;

import items.Item;
import items.wereables.OneHandSword;
import items.wereables.SmallShield;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import magic.FireRing;
import magic.Spell;
import grammars.english.*;
import grammars.grammars.GrammarIndividual;
import grammars.grammars.GrammarSelector;
import grammars.grammars.GrammarSelectorNP;
import grammars.grammars.GrammarSelectorS;
import grammars.grammars.GrammarsGeneral;
import grammars.grammars.GrammarsOperational;
import grammars.grammars.GrammarsRetrieval;
import grammars.grammars.WordsGrammar;
import grammars.parsing.JSONParsing;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import characters.active.ActiveCharacter;
import characters.active.enemies.Goblin;
import util.RandUtil;
import util.Tuple;
import map.Map;
import map.Room;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;


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
	static JsonObject grammarAction;
	
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
	
	public static void _bindKeys() {
		movementInput = new Integer[] {keysMap.get("left"), keysMap.get("right"), keysMap.get("down"), keysMap.get("up")};
		inventoryInput = new Integer[] {keysMap.get("item1"), keysMap.get("item2"), keysMap.get("item3"), keysMap.get("item4"),
				keysMap.get("item5"), keysMap.get("item6")};
		pickItemInput = new Integer[] {keysMap.get("pickItem")};
		attackInput = new Integer[] {keysMap.get("attack")};
		spellInput = new Integer[] {keysMap.get("spell1"), keysMap.get("spell2")};
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
		user = new ActiveCharacter("hero", "", null, null, null, 
				40, 0, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, 0);
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
		user = new ActiveCharacter("", "", map, map.obtainRoomByPosition(pos), pos, 
				40, 0, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, 0);
		_setKeyMap();
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		WereableWeapon oneHandSword = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		WereableWeapon oneHandSword2 = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		
		Goblin goblin = new Goblin(map, map.obtainRoomByPosition(pos), pos);
		Goblin goblin2 = new Goblin(map, map.obtainRoomByPosition(pos), pos);
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
	
	public static void _inventoryAction(int i){
		if (debug) {
    		System.out.println(user.getWeaponsEquipped().size());
    	}
		
		int itemNumber = i % keysMap.get("item1");
		if (itemNumber + 1 <= user.getInventory().size()) {
			user.useItem(user.getInventory().get(itemNumber));
	    	j.cls();
		}
		if (debug) {
			System.out.println(user.getWeaponsEquipped().size());
		}
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void _pickItemAction(){
		for (Item item: user.getRoom().getItemsRoom()){
			System.out.println("The items are: " + item.getName());
		}
		if (user.pickItem(user.getPosition(), user.getRoom())){
			hasChanged = true;
    	}
		j.cls();
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void _attackAction(){
		if (map.getMonstersPosition(user).size() > 0) {
			ActiveCharacter monster = user.weaponAttack();
			if (monster.getLife() <= 0) {
				hasChanged = true;
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
				user.getRoom().monsterTurn(user);
				if (hasMoved) {
					printEverything(true);
					j.refresh();
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		            hasMoved = false;
				}
				int i = j.inkey().code;
				System.out.println("Code" + i);
				j.cls();
				
				System.out.println(i);
	            if (isMovementInput(i)){
	            	_moveCharacterAction(i);
	            }
	            else if (isInventoryInput(i)) {
	            	_inventoryAction(i);
	            }
	            else if (isPickItemInput(i)) {
	            	_pickItemAction();
	            }
	            else if (isAttackInput(i)) {
	            	_attackAction();
	            } 
	            else if (isSpellInput(i)) {
	            	_spellAction(i);
	            } else {
	            	printEverything(true);
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	            }
	            
	            if (debug) {
	            	System.out.println("Vida user: " + user.getLife());
	            }
	            
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
		grammarAction = JSONParsing.getElement(rootObj, "ATTACK").getAsJsonObject();
//		ChangeKeyBinding.editPropertiesFile(j);
//		JLabel message = new JLabel();
//		message.setText("Hola");
//		message.requestFocusInWindow();
//		JOptionPane.showMessageDialog(null, message, "", JOptionPane.PLAIN_MESSAGE);
//		if (!testMode){
//			gameFlow();
//		}
		
		// NP Grammar example
//		JsonParser parser = new JsonParser();
//		JsonObject rootObj = parser.parse(new FileReader("./src/grammars/english/objectGrammarTest.json")).getAsJsonObject();
//		JsonObject rootObjWords = parser.parse(new FileReader("./src/grammars/english/wordsEnglish.json")).getAsJsonObject();
//		Item item = new SmallShield("", 0, 0, 0, user, map, roomCharacter, null, 0, 0, false);
//		GrammarsGeneral grammarGeneral = new GrammarsGeneral(rootObj);
//		GrammarIndividual grammarIndividual = grammarGeneral.getRandomGrammar();
//		GrammarSelectorNP selector = new GrammarSelectorNP(grammarIndividual, rootObjWords, item);
//		System.out.println(selector.getRandomSentence());
//		selector.getRandomSentence();
		
		// S Grammar example
		Item shield1 = new SmallShield("", 0, 0, 0, user, map, roomCharacter, null, 0, 0, false);
		Item shield2 = new SmallShield("", 0, 0, 0, user, map, roomCharacter, null, 0, 0, false);
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(shield1);
		items.add(shield2);
		GrammarsGeneral grammarGeneral = new GrammarsGeneral(grammarAction);
		GrammarIndividual grammarIndividual = grammarGeneral.getRandomGrammar();
		GrammarSelectorS selector = new GrammarSelectorS(grammarIndividual, rootObjWords, items, "attack");
		System.out.println(selector.getRandomSentence());
		
	}
}
