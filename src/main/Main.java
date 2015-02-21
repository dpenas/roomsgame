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
	public static boolean debug = true;
	public static boolean testMode = true;
	static Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	static Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(20, 20);
	static Integer[] movementInput = new Integer[] {0, 1, 2, 3};
	static Integer[] inventoryInput = new Integer[] {131, 132, 133, 134, 135, 136};
	
	
	public static Room getRandomRoom(Map map){
		return map.getRooms().get(RandUtil.RandomNumber(0, map.getRooms().size()));
	}
	
	public static boolean isMovementInput(int key){
		return Arrays.asList(movementInput).contains(key);
	}
	
	public static boolean isInventoryInput(int key){
		return Arrays.asList(inventoryInput).contains(key);
	}

	public static void main(String[] args) throws IOException {

		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		
		if (!testMode){
			Map map = new Map(initial_point, final_point);
			Room roomCharacter = getRandomRoom(map);
			char previousPositionChar = '.';
			char previousPositionChar2 = '.';
			boolean firstTime = true;
			ActiveCharacter user = new ActiveCharacter("", "", "", map, roomCharacter, roomCharacter.getRandomInsidePosition(), 
					40, 10, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
					new ArrayList<WereableArmor>(), 100, 100, 0,
					new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4);
			
			WSwingConsoleInterface j = new WSwingConsoleInterface("asdasd");
			j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
			LifePotion lifePotion30 = new LifePotion(0, 10, "", null, null, null, null, 30);
			lifePotion30.setCharacter(user);
			LifePotion lifePotion40 = new LifePotion(0, 10, "", null, null, null, null, 30);
			lifePotion40.setCharacter(user);
			LifePotion lifePotion50 = new LifePotion(0, 10, "", null, null, null, null, 30);
			lifePotion50.setCharacter(user);
			WereableWeapon oneHandSword = new OneHandSword("", 0, 0, 0, user, null, null,
					null, 0, 0, false);
			
			ArrayList<Item> inventory = new ArrayList<Item>();
			inventory.add(lifePotion30);
			inventory.add(lifePotion40);
			inventory.add(lifePotion50);
			inventory.add(oneHandSword);
			user.setInventory(inventory);
			map.printBorders(j, user);
			map.printInside(j, user);
			user.printInventory(user.getInventory(), j, 22, 0);
			j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
			user.printInventory(user.getInventory(), j, 22, 0);
			j.refresh();
			for (;;) {
				int i = j.inkey().code;
				j.cls();
				map.printBorders(j, user);
				map.printInside(j, user);
				user.printInventory(user.getInventory(), j, 22, 0);
				j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	            System.out.println(i);
	            Tuple<Integer, Integer> previousPosition = user.getPosition();
	            Tuple<Integer, Integer> newPosition = RandUtil.inputMoveInterpretation(i, user);
	            user.setLife(80);
	            if (isMovementInput(i)){
		            if (user.move(newPosition)){
		            	user.setVisiblePositions();
		            	map.printBorders(j, user);
		    			map.printInside(j, user);
		            	previousPositionChar = previousPositionChar2;
		            	previousPositionChar2 = j.peekChar(newPosition.y, newPosition.x);
		            	
		            	if (j.peekChar(newPosition.y, newPosition.x) == '.'){
		            		j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
			            	j.print(previousPosition.y, previousPosition.x, previousPositionChar, 12);
			            	
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
	            else if (isInventoryInput(i)){
	            	System.out.println(user.getWeaponsEquipped().size());
	            	user.useItem(user.getInventory().get(i%131));
	            	j.cls();
					map.printBorders(j, user);
					map.printInside(j, user);
					user.printInventory(user.getInventory(), j, 22, 0);
					System.out.println(user.getWeaponsEquipped().size());
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	            }
				j.refresh();
			}
		}
	}
}
