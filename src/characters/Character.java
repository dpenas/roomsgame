package characters;

import java.util.ArrayList;
import items.Item;

import map.Map;
import map.Room;
import util.Tuple;

public abstract class Character {
	private String name;
	private String description;
	private String gender;
	private int weight; // Character's weight
	private int carryWeight; // Max weight we can carry
	private int actualCarryWeight;
	private int length;
	private Map map;
	private Room room;
	private Tuple<Integer, Integer> position;
	private ArrayList<Item> inventory;
	
	public Character(String name, String description, String gender, Map map,
			Room room, Tuple<Integer, Integer> position, int weight, int length, int carryWeight,
			int actualCarryWeight) {
		super();
		this.name = name;
		this.description = description;
		this.gender = gender;
		this.map = map;
		this.room = room;
		this.position = position;
		this.weight = weight;
		this.length = length;
		this.carryWeight = carryWeight;
		this.actualCarryWeight = actualCarryWeight;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String gender() {
		return gender;
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

	public Tuple<Integer, Integer> getPosition() {
		return position;
	}

	public void setPosition(Tuple<Integer, Integer> position) {
		this.position = position;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

}
