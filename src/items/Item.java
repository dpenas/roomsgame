package items;

import items.consumables.Consumable;
import items.wereables.Wereable;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import java.util.ArrayList;

import util.Tuple;
import map.Map;
import map.Room;
import characters.Character;

public abstract class Item {

	private String name;
	private String description;
	private String gender;
	private int weight;
	private int space;
	private Character character;
	private Map map;
	private Room room;
	private ArrayList<String> nameAttributes;
	private Tuple<Integer, Integer> position;
	private String symbolRepresentation;

	public Item(String name, ArrayList<String> nameAttributes, String description, 
			String gender, int weight, int space, Character character, 
			Map map, Room room, Tuple<Integer, Integer> position, String symbolRepresentation) {
		super();
		this.nameAttributes = nameAttributes;
		this.name = name;
		this.description = description;
		this.gender = gender;
		this.weight = weight;
		this.space = space;
		this.character = character;
		this.map = map;
		this.room = room;
		this.position = position;
		this.symbolRepresentation = symbolRepresentation;
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

	public String getDescription() {
		return description;
	}

	public String getGender() {
		return gender;
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

	public Tuple<Integer, Integer> getPosition() {
		return position;
	}

	public void setPosition(Tuple<Integer, Integer> position) {
		this.position = position;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getNameAttributes() {
		return nameAttributes;
	}

	public void setNameAttributes(ArrayList<String> nameAttributes) {
		this.nameAttributes = nameAttributes;
	}

	public String getSymbolRepresentation() {
		return symbolRepresentation;
	}

	public void setSymbolRepresentation(String symbolRepresentation ) {
		this.symbolRepresentation = symbolRepresentation ;
	}

}
