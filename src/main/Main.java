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
import grammars.grammars.PrintableObject;
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
import map.Map;
import map.Room;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import net.slashie.util.Pair;
import util.ActionHandler;
import util.JTextAreaWithListener;
import util.MessageDescriptionsUtil;
import util.RandUtil;
import util.Tuple;


public class Main {
	public static String language = new String("EN");
	public static ResourceBundle messagesWereables, keyBinding;
	public static int countElements;
	public static HashMap<String, Integer> keysMap;
	public static boolean debug = false;
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
	public static Integer[] descriptionSpellInput;
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
	public static boolean hasChanged = false;
	static boolean hasMoved = false;
	static char previousPositionChar = '.';
	static char previousPositionChar2 = '.';
	static char deepnessScore = 0;
	static boolean isNumericDescription = false;
	static boolean hasUsedPortal = false;
	public static boolean hasEquipedItem = false;
	public static boolean hasThrownItem = false;
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
	static ActionHandler actionHandler;
	static GrammarsGeneral grammarAttack;
	static GrammarsGeneral grammarPickItem;
	static GrammarsGeneral grammarUnvalidDescription;
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
		descriptionSpellInput = new Integer[] {keysMap.get("descSpell")};
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
		map._printInformationMonsters(j, user, rootObjWords);
		if (needsToPrintGroundObjects) {
			user._printGroundObjects(j, rootObjWords);
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
		actionHandler = new ActionHandler(keysMap, user, rootObj, rootObjWords);
	}
	
	public static void _initializeMap() {
		int min_rooms = 5;
		map = new Map(initial_point, final_point);
		while (map.getRooms().size() < min_rooms || !map.hasPortals()) {
			map = new Map(initial_point, final_point);
		}
		map.initialize(user);
		printEverything(true);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), arrayColors[selectedColor][0]);
		j.refresh();
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
	
	public static void useAndWithItem(Item item) {
		String message = JSONParsing.getTranslationWord("and", "OTHERS", rootObjWords);
		GrammarIndividual grammarIndividual = grammarGeneralObj.getRandomGrammar();
		GrammarSelectorNP selector = new GrammarSelectorNP(grammarIndividual, rootObjWords, item, "GENERAL");
		printMessage(message + " " + selector.getRandomSentenceTranslated());
	}
	
	public static void _messageUnvalid() {
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
    		actionHandler._spellAction(itemCode, usePronoun());
    		canUsePronoun = true;
    		printEverything(true);
    		setFlagsToFalse();
    	}
    	canUsePronoun = true;
    	printEverything(true);
	}
	
	public static void throwAction(int itemCode) {
		if (isInputType(inventoryInput, itemCode)) {
    		actionHandler._throwItem(itemCode, usePronoun());
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
		if (isInputType(movementInput, i)){
        	doMonstersTurn = true;
        	_moveCharacterAction(i);
        	setFlagsToFalse();
        }
        else if (isInputType(inventoryInput, i)) {
        	doMonstersTurn = true;
        	actionHandler._inventoryAction(i, usePronoun());
        	canUsePronoun = true;
        	printEverything(false);
        	hasUnequipedItem = false;
        	hasThrownItem = false;
        	hasPickedItem = false;
        }
        else if (isInputType(pickItemInput, i)) {
        	doMonstersTurn = true;
        	actionHandler._pickItemAction(usePronoun(), hasPickedItem);
        	canUsePronoun = true;
        	printEverything(true);
        	hasEquipedItem = false;
        	hasUnequipedItem = false;
        	hasThrownItem = false;
        }
        else if (isInputType(attackInput, i)) {
        	doMonstersTurn = true;
        	actionHandler._attackAction(usePronoun());
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
        	actionHandler._descriptionAction(i, usePronoun(), isNumericDescription);
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
        } else if (isInputType(descriptionSpellInput, i)) {
        	MessageDescriptionsUtil.describeSpells(user, rootObjWords, grammarSimpleVerb);
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
				MessageDescriptionsUtil._messageDescriptionDead(user, true, grammarAdjectiveDescription);
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
		grammarGeneralDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "GENERAL").getAsJsonObject());
		grammarUnvalidDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCUNVALID").getAsJsonObject());
		grammarAdjectiveDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "DESCRIPTIONADJECTIVE").getAsJsonObject());
		grammarMissDescription = new GrammarsGeneral(JSONParsing.getElement(rootObj, "ATTACKMISS").getAsJsonObject());
		grammarSimpleVerb = new GrammarsGeneral(JSONParsing.getElement(rootObj, "SIMPLEVERB").getAsJsonObject());
		grammarGeneralObj = new GrammarsGeneral(JSONParsing.getElement(rootObjGrammar, "GENERAL").getAsJsonObject());
		if (!testMode){
			gameFlow();
		}
	}
}