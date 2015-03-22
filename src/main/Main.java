package main;

import items.Item;
import items.consumables.LifePotion;
import items.wereables.OneHandSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

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
	public static ResourceBundle messagesWereables;
	public static int countElements;
	public static boolean debug = false;
	public static boolean testMode = false;
	public static char[] usedSymbols = {'.', 'P', 'G', 'A'};
	static Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	static Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(20, 20);
	static Integer[] movementInput = new Integer[] {0, 1, 2, 3};
	static Integer[] inventoryInput = new Integer[] {131, 132, 133, 134, 135, 136};
	static Integer[] pickItemInput = new Integer[] {68};
	static Integer[] attackInput = new Integer[] {87};
	static Map map = new Map(initial_point, final_point);
	static Tuple<Integer, Integer> pos = new Tuple<Integer, Integer>(1,1);
	static Room roomEnemy = getRandomRoom(map);
	static Room roomCharacter = getRandomRoom(map);
	static WSwingConsoleInterface j = new WSwingConsoleInterface("asdasd");
	static ActiveCharacter user = new ActiveCharacter("", "", "", map, roomCharacter, roomCharacter.getRandomInsidePosition(), 
			40, 0, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
			new ArrayList<WereableArmor>(), 100, 100, 0,
			new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, 0);
	static boolean firstTime = true;
	static boolean hasChanged = false;
	static char previousPositionChar = '.';
	static char previousPositionChar2 = '.';
	
	
	public static Room getRandomRoom(Map map){
		return map.getRooms().get(RandUtil.RandomNumber(0, map.getRooms().size()));
	}
	
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
	
	public static void printEverything(Map map, WSwingConsoleInterface j, ActiveCharacter user){
		map.printBorders(j, user);
		map.printInside(j, user);
		map.printItems(j, user);
		map.printMonsters(j, user);
		_printInventoryUser();
		_printLifeUser();
		_printInformationMonsters(true);
		_printGroundObjects();
	}
	
	public static void _moveCharacterAction(int i){
		Tuple<Integer, Integer> previousPosition = user.getPosition();
        Tuple<Integer, Integer> newPosition = RandUtil.inputMoveInterpretation(i, user);
		if (user.move(newPosition)){
        	user.setVisiblePositions();
        	printEverything(map, j, user);
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
        }
	}
	
	public static void _printInventoryUser(){
		user.printInventory(user.getInventory(), j, map.global_fin().x + 1, 0);
	}
	
	public static void _printLifeUser(){
		user._printLife(j, 0, map.global_fin().y + 1);
	}
	
	public static void _printInformationMonsters(boolean firstCall) {
		int count = 0;
		int numMonsters = 0;
		for (ActiveCharacter monster : user.getRoom().getMonstersPosition(user.getPosition())) {
			// TODO: Change this to translation
			if (firstCall){
				countElements += 1;
				if (!monster.isDead() && count == 0){
					j.print(map.global_fin().y, countElements, "Monsters: ");
					count++;
				}
				
				if (!monster.isDead() || monster.isFirstTimeDead()){
					countElements += 1;
					monster.printMonstersInformation(j, map.global_fin().y, countElements);
					if (monster.isDead()){
						monster.setFirstTimeDead(false);
					}
				}
			} else {
				if (!monster.isDead() || monster.isFirstTimeDead()){
					//TODO: Change this to support more than 1 monster
					System.out.println(user.getRoom().getMonstersPosition(user.getPosition()).size());
					monster._printLife(j, countElements + 1, map.global_fin().y);
				}
			}
		}
	}
	
	public static void _printGroundObjects(){
		if (user.getRoom().getItemsPosition(user.getPosition()).size() > 0){
			countElements += 1;
			j.print(map.global_fin().y, countElements, "Items: ");
		}
		for (Item item : user.getRoom().getItemsPosition(user.getPosition())) {
			countElements += 1;
			item.printItemsInformation(j, map.global_fin().y, countElements);
		}
	}
	
	public static void _initialize(){
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		LifePotion lifePotion30 = new LifePotion(0, 10, "", null, null, null, null, 30);
		lifePotion30.setCharacter(user);
		LifePotion lifePotion40 = new LifePotion(0, 10, "", null, null, null, null, 30);
		lifePotion40.setCharacter(user);
		LifePotion lifePotion50 = new LifePotion(0, 10, "", null, null, null, null, 30);
		lifePotion50.setCharacter(user);
		LifePotion lifePotion60 = new LifePotion(0, 10, "", null, null, null, pos, 30);
		map.putItemRoom(lifePotion60);
		WereableWeapon oneHandSword = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		WereableWeapon oneHandSword2 = new OneHandSword("", 0, 0, 100, user, null, null,
				null, 0, 0, true);
		
		Goblin goblin = new Goblin(map, roomEnemy, roomEnemy.getRandomPosition(), 0, new ArrayList<Item>());
		goblin.putItemInventory(oneHandSword2);
		roomEnemy.getMonsters().add(goblin);
		
		ArrayList<Item> inventory = new ArrayList<Item>();
		inventory.add(lifePotion30);
		inventory.add(lifePotion40);
		inventory.add(lifePotion50);
		inventory.add(oneHandSword);
		user.setInventory(inventory);
		user.setLife(80);
		printEverything(map, j, user);
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
		j.refresh();
	}
	
	public static void _inventoryAction(int i){
		if (debug) {
    		System.out.println(user.getWeaponsEquipped().size());
    	}
    	user.useItem(user.getInventory().get(i%131));
    	j.cls();
    	printEverything(map, j, user);
		if (debug) {
			System.out.println(user.getWeaponsEquipped().size());
		}
		j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	}
	
	public static void _pickItemAction(){
		for (Item item: user.getRoom().getItemsRoom()){
			System.out.println("The items are: " + item.getName());
		}
		if (user.pickItem(user.getPosition(), user.getRoom())){
    		j.cls();
    		printEverything(map, j, user);
			j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
			hasChanged = true;
    	}
		
	}
	
	public static void _attackAction(){
		if (map.getMonstersPosition(user).size() > 0){
    		ActiveCharacter monster = map.getMonstersPosition(user).get(0);
    		if (debug) {
    			System.out.println("Vida monster: " + map.getMonstersPosition(user).get(0).getLife());
    		}
    		user.attack(monster);
    		if (monster.getLife() <= 0){
    			if (debug) {
    				System.out.println("Item position: ");
        			System.out.println(monster.getInventory().get(0).getPosition().x);
        			System.out.println(monster.getInventory().get(0).getPosition().y);
    			}
    			hasChanged = true;
    		}
    		System.out.println("Vida monster: " + map.getMonstersPosition(user).get(0).getLife());
    		_printInformationMonsters(false);
    		_printLifeUser();
    	}
	}

	public static void main(String[] args) throws IOException {

		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		
		if (!testMode){
			
			_initialize();
			for (;;) {
				countElements = 2;
				if (debug) {
					System.out.println("Vida user: " + user.getLife());
				}
				int i = j.inkey().code;
				j.cls();
				printEverything(map, j, user);
				j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
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
	            
	            user.getRoom().monsterTurn(user);
	            if (debug) {
	            	System.out.println("Vida user: " + user.getLife());
	            }
	            
				j.refresh();
			}
		}
	}
}
