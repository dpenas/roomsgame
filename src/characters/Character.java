package characters;

import map.Map;
import map.Room;
import util.Tuple;

public abstract class Character {
	private String name;
	private String description;
	private boolean isMale;
	private Map map;
	private Room room;
	private Tuple<Integer, Integer> position;
	
	public Character(String name, String description, boolean isMale, Map map,
			Room room, Tuple<Integer, Integer> position) {
		super();
		this.name = name;
		this.description = description;
		this.isMale = isMale;
		this.map = map;
		this.room = room;
		this.position = position;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isMale() {
		return isMale;
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

}
