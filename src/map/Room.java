package map;

import java.util.ArrayList;

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



	public static void main(String[] args) {
		
	}

}
