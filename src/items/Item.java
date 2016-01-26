package items;

import items.consumables.Consumable;
import items.wereables.Wereable;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import java.util.ArrayList;

import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import util.RandUtil;
import util.Tuple;
import map.Map;
import map.Room;
import characters.Character;
import grammars.grammars.PrintableObject;

public abstract class Item extends PrintableObject{

	private int weight;
	private int space;
	private Character character;
	private Map map;
	private Room room;
	private String symbolRepresentation;

	public Item(String name, ArrayList<String> adjectives, String description, 
			String gender, int weight, int space, Character character, 
			Map map, Room room, Tuple<Integer, Integer> position, String symbolRepresentation) {
		super(name, description, adjectives, position);
		this.weight = weight;
		this.space = space;
		this.character = character;
		this.map = map;
		this.room = room;
		this.symbolRepresentation = symbolRepresentation;
	}
	
	public void setAttributesFromCharacter(Character c){
		this.setMap(c.getMap());
		this.setRoom(c.getRoom());
		this.setPosition(c.getPosition());
	}
	
	public void printItemsInformation(WSwingConsoleInterface j, int initPos_i, int initPos_j) {
		j.print(initPos_i, initPos_j, this.getName());
	}
	
	public boolean isWereableItem(){
		return Wereable.class.isInstance(this);
	}
	
	public boolean isWereableArmor(){
		return WereableArmor.class.isInstance(this);
	}
	
	public boolean isWereableWeapon(){
		return WereableWeapon.class.isInstance(this);
	}
	
	public boolean isConsumableItem(){
		return Consumable.class.isInstance(this);
	}

	public int getWeight() {
		return weight;
	}

	public int getSpace() {
		return space;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public String getSymbolRepresentation() {
		return symbolRepresentation;
	}

	public void setSymbolRepresentation(String symbolRepresentation ) {
		this.symbolRepresentation = symbolRepresentation ;
	}

}
