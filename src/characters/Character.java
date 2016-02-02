package characters;

import java.util.ArrayList;

import grammars.grammars.PrintableObject;
import items.Item;
import map.Map;
import map.Room;
import util.Tuple;

public abstract class Character extends PrintableObject {
	private String name;
	private String description;
	private int weight; // Character's weight
	private int carryWeight; // Max weight we can carry
	private int actualCarryWeight;
	private int length;
	private Map map;
	private Room room;
	private String symbolRepresentation;
	private ArrayList<Item> inventory;
	
	public Character(String name, String description, Map map,
			Room room, Tuple<Integer, Integer> position, int weight, int length, int carryWeight,
			int actualCarryWeight, ArrayList<Item> inventory, String symbolRepresentation, ArrayList<String> adjectives) {
		super(name, description, adjectives, position);
		this.name = name;
		this.description = description;
		this.map = map;
		this.room = room;
		this.weight = weight;
		this.length = length;
		this.carryWeight = carryWeight;
		this.actualCarryWeight = actualCarryWeight;
		this.inventory = inventory;
		this.symbolRepresentation = symbolRepresentation;
		ArrayList<String> newPrepositions = new ArrayList<String>();
		newPrepositions.add("against");
		this.setPrepositions(newPrepositions);
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Map getMap(){
		return map;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public ArrayList<Item> getInventory() {
		return inventory;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public int getCarryWeight(){
		return carryWeight;
	}

	public int getActualCarryWeight() {
		return actualCarryWeight;
	}

	public void setActualCarryWeight(int actualCarryWeight) {
		this.actualCarryWeight = actualCarryWeight;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setCarryWeight(int carryWeight) {
		this.carryWeight = carryWeight;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}

	public String getSymbolRepresentation() {
		return symbolRepresentation;
	}

	public void setSymbolRepresentation(String symbolRepresentation) {
		this.symbolRepresentation = symbolRepresentation;
	}

}
