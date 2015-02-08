package map;

import java.util.ArrayList;

import util.RandUtil;
import util.Tuple;

/**
 * A room is randomly generated depending on the given size and
 * number of elements in it. These values don't have to be specified.
 * @author Dario
 *
 */
public class Room {
	
	/**
	 *  individual_x and individual_y represent the length of the room
	 *  itself, whereas global_x and global_y refers to the location of the
	 *  room inside a map
	 */
	private Tuple<Integer, Integer> individual_initial;
	private Tuple<Integer, Integer> individual_final;
	private Tuple<Integer, Integer> global_initial;
	private Tuple<Integer, Integer> global_final;
	private ArrayList<Door> doors = new ArrayList<Door>();
	private ArrayList<Room> connected_rooms = new ArrayList<Room>();
	
	public Room(Tuple<Integer, Integer> global_initial, Tuple<Integer, Integer> global_final){
		int individual_final_x = global_final.x - global_initial.x;
		int individual_final_y = global_final.y - global_initial.y;
		this.individual_initial = new Tuple<Integer, Integer>(0, 0);
		this.individual_final = new Tuple<Integer, Integer>(individual_final_x, individual_final_y);
		this.global_initial = global_initial;
		this.global_final = global_final;
	}

	/**
	 * Given the length of a union between rooms, it gives a series
	 * of numbers where the door should be placed in that area.
	 * @param length of the column/row where both rooms collide
	 * @return List of numbers where the rooms should
	 */
	
	public ArrayList<Integer> AssignRandomDoors(int length){
		
		int i = 0;
		int numDoors;
		int randNumber;
		int resultDiv = length/10;
		ArrayList<Integer> finalRooms = new ArrayList<Integer>();
		if (resultDiv <= 0){ 
			numDoors = 1;
		}
		else {
			numDoors = resultDiv;
		}
		
		while (i < numDoors){
			randNumber = RandUtil.RandomNumber(0, length);
			// Only inserts if the number is not in the array already.
			if (!finalRooms.contains(randNumber)){
				finalRooms.add(randNumber);
				i++;
			}
		}
		
		return finalRooms;
	}
	
	public boolean isMapPositionHere(Tuple<Integer, Integer> position){
		Tuple<Integer, Integer> initialPoint = this.getGlobal_initial();
		Tuple<Integer, Integer> finalPoint = this.getGlobal_final();
		if (position.x >= initialPoint.x && position.y >= initialPoint.y){
			if (position.x <= finalPoint.x && position.y <= finalPoint.y){
				return true;
			}
		}
		return false;
	}

	public Tuple<Integer, Integer> getIndividual_initial() {
		return individual_initial;
	}

	public void setIndividual_initial(Tuple<Integer, Integer> individual_initial) {
		this.individual_initial = individual_initial;
	}

	public Tuple<Integer, Integer> getIndividual_final() {
		return individual_final;
	}

	public void setIndividual_final(Tuple<Integer, Integer> individual_final) {
		this.individual_final = individual_final;
	}

	public Tuple<Integer, Integer> getGlobal_initial() {
		return global_initial;
	}

	public void setGlobal_initial(Tuple<Integer, Integer> global_initial) {
		this.global_initial = global_initial;
	}

	public Tuple<Integer, Integer> getGlobal_final() {
		return global_final;
	}

	public void setGlobal_final(Tuple<Integer, Integer> global_final) {
		this.global_final = global_final;
	}

	public ArrayList<Door> getDoors() {
		return doors;
	}

	public void setDoors(ArrayList<Door> doors) {
		this.doors = doors;
	}

	public ArrayList<Room> getConnected_rooms() {
		return connected_rooms;
	}

	public void setConnected_rooms(ArrayList<Room> connected_rooms) {
		this.connected_rooms = connected_rooms;
	}

}
