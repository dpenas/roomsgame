package map;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import util.Tuple;

public class Door {
	
	private Tuple<Integer, Integer> positionRoom1;
	private Tuple<Integer, Integer> positionRoom2;
	private Room room1;
	private Room room2;
	
	public Door(Tuple<Integer, Integer> positionRoom1,
			Tuple<Integer, Integer> positionRoom2, Room room1, Room room2) {
		super();
		this.positionRoom1 = positionRoom1;
		this.positionRoom2 = positionRoom2;
		this.room1 = room1;
		this.room2 = room2;
	}
	
	public Tuple<Integer, Integer> getPositionRoom (Tuple<Integer, Integer> tuple){
		if (room1.isMapPositionHere(tuple)){
			return positionRoom1;
		}
		if (room2.isMapPositionHere(tuple)){
			return positionRoom2;
		}
		return null;
	}
	
	public Tuple<Integer, Integer> getPositionRoom(ActiveCharacter character) {
		if (character.getRoom().equals(room1)) {
			return positionRoom1;
		}
		if (character.getRoom().equals(room2)) {
			return positionRoom2;
		}
		return null;
	}
	
	public ArrayList<String> getAdjectives() {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("old");
		adjectives.add("creepy");
		return adjectives;
	}
	
	public Tuple<Integer, Integer> getPositionRoom1() {
		return positionRoom1;
	}
	public void setPositionRoom1(Tuple<Integer, Integer> positionRoom1) {
		this.positionRoom1 = positionRoom1;
	}
	public Tuple<Integer, Integer> getPositionRoom2() {
		return positionRoom2;
	}
	public void setPositionRoom2(Tuple<Integer, Integer> positionRoom2) {
		this.positionRoom2 = positionRoom2;
	}
	public Room getRoom1() {
		return room1;
	}
	public void setRoom1(Room room1) {
		this.room1 = room1;
	}
	public Room getRoom2() {
		return room2;
	}
	public void setRoom2(Room room2) {
		this.room2 = room2;
	}
}
