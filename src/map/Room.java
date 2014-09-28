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
	private ArrayList<int[][]> doors;
	private ArrayList<Room> connected_rooms;
	
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

	public static void main(String[] args) {

	}

}
