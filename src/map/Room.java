package map;

import java.util.ArrayList;
import java.util.Random;

/**
 * A room is randomly generated depending on the given size and
 * number of elements in it. These values doesn't have to be specified.
 * @author Darío
 *
 */
public class Room {
	
	/**
	 *  individual_x and individual_y represent the length of the room
	 *  itself, whereas global_x and global_y refers to the location of the
	 *  room inside a map
	 */
	
	private int individual_x;
	private int individual_y;
	private int global_x;
	private int global_y;
	private ArrayList<int[][]> doors;
	private ArrayList<Room> connected_rooms;
	
	public Room(int individual_x, int individual_y) {
		this.individual_x = individual_x;
		this.individual_y = individual_y;
	}
	
	/**
	 * TODO: This shouldn't be here. Maybe create a utility class.
	 * It returns a number between two given values
	 * @param interval
	 * @return
	 */
	public int RandomNumber(int minValue, int maxValue){
		Random randomGenerator = new Random();
		int randomIntValue = randomGenerator.nextInt(maxValue - minValue) + minValue;
		return randomIntValue;
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
		if (resultDiv <= 0) 
			numDoors = 1;
		else {
			numDoors = resultDiv;
		}
		
		while (i < numDoors){
			randNumber = RandomNumber(0, length);
			// Only inserts if the number is not in the array already.
			if (!finalRooms.contains(randNumber)){
				finalRooms.add(randNumber);
				i++;
			}
		}
		
		return finalRooms;
	}

	public static void main(String[] args) {
		Room r = new Room(10, 10);
		ArrayList<Integer> test = new ArrayList<Integer>();
		test = r.AssignRandomDoors(5);
		
		for (int i = 0; i < test.size(); i++){
			System.out.println(test.get(i));
		}
	}

}
