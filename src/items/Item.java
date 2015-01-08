package items;

import util.Tuple;
import map.Map;
import map.Room;
import characters.Character;

public abstract class Item {

	private String description;
	private String gender;
	private int weight;
	private int space;
	private Character character;
	private Map map;
	private Room room;
	private Tuple<Integer, Integer> position;

	public Item(String description, String gender, int weight, int space,
			Character character, Map map, Room room,
			Tuple<Integer, Integer> position) {
		super();
		this.description = description;
		this.gender = gender;
		this.weight = weight;
		this.space = space;
		this.character = character;
		this.map = map;
		this.room = room;
		this.position = position;
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

}
