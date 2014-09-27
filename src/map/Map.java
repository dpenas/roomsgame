package map;

import java.util.ArrayList;

import util.RandUtil;
import util.Tuple;

/**
 * TODO RELEVANT:
 * - Complete nextPoint and initialize_rooms_map, which should call 
 * the other functions and create the actual map
 * 
 * TODO ADDITIONAL: 
 * - Map of maps? So we can teleport to other maps and stuff.
 *
 */

public class Map {

	private Tuple<Integer, Integer> global_init;
	private Tuple<Integer, Integer> global_fin;
	private int real_x;
	private int real_y;
	byte[][] free_room;
//	private int free_room_x;
//	private int free_room_y;
	private int size;

	/**
	 * Creates a random map, which is a collection of rooms
	 * @param global_x
	 * @param global_y
	 */
	
	public Map(Tuple<Integer, Integer> global_init, Tuple<Integer, Integer> global_fin){
		this.set_global_init(global_init);
		this.set_global_init(global_fin);
//		this.set_free_room_x(0);
//		this.set_free_room_y(0);
		real_x = this.global_fin().x - this.global_init().x;
		real_y = this.global_fin().y - this.global_init().y;
		size = real_x * real_y;
		free_room = new byte[real_x][real_y];
		initializeMatrixZero(free_room);
		this.initialize_rooms_map();
	}
	
	/**
	 * Given the dimensions of the map, it returns the number of rooms
	 * that the map will have
	 * @return
	 */
	public int obtainNumberRooms(){
		double reduced_size = size/100;
		int min_num = (int) Math.floor(reduced_size);
		int max_num = (int) Math.ceil(reduced_size);
		return RandUtil.RandomNumber(min_num, max_num);
	}
	
	/**
	 * Given a byte matrix, it initializes it to 0
	 * @param matrix
	 * @return
	 */
	public void initializeMatrixZero(byte[][] matrix){
		int real_x = this.global_fin().x - this.global_init().x;
		int real_y = this.global_fin().y - this.global_init().y;
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				matrix[i][j] = 0;
			}
		}
	}
	
	/**
	 * 
	 * @return Tuple of the position of the next room that we must extend
	 */
	public Tuple<Integer, Integer> obtainAvailableRooms(){
		int real_x = this.global_fin().x - this.global_init().x;
		int real_y = this.global_fin().y - this.global_init().y;
		ArrayList<Tuple<Integer, Integer>> possibleExtensionPoints = new ArrayList<>();
		if (this.free_room[0][0] == 0){
			possibleExtensionPoints.add(new Tuple<Integer, Integer>(0,0));
		}
		
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				if (i == 0 && j != 0){
					if (free_room[0][j] == 0 && free_room[0][j-1] == 1){
						possibleExtensionPoints.add(new Tuple<Integer, Integer>(0, j-1));
					}
				}
				if (j == 0 && i != 0){
					if (free_room[i][0] == 0 && free_room[i-1][0] == 1){
						possibleExtensionPoints.add(new Tuple<Integer, Integer>(i-1, 0));
					}
				}
				if ((free_room[i-1][j-1] == 1 && free_room[i][j] == 0)){
					possibleExtensionPoints.add(new Tuple<Integer, Integer>(i-1, j-1));
				}
			}
		}
		
		int position_random_selected = RandUtil.RandomNumber(0, possibleExtensionPoints.size());
		
		return possibleExtensionPoints.get(position_random_selected);

	}
	
	
	/**
	 * Sets to 1 the space of the two rooms defined by the tuples in the free room array
	 * @param tuple1
	 * @param tuple2
	 */
	public void createRoomMatrix(Tuple<Integer, Integer> tuple1, Tuple<Integer, Integer> tuple2){
		int lowestX;
		int highestX;
		int lowestY;
		int highestY;
		
		if (tuple1.x < tuple2.x){
			lowestX = tuple1.x;
			highestX = tuple2.x;
		} else {
			lowestX = tuple2.x;
			highestX = tuple1.x;
		}
		
		if (tuple1.y < tuple2.y){
			lowestY = tuple1.y;
			highestY = tuple2.y;
		} else {
			lowestY = tuple2.y;
			highestY = tuple1.y;
		}
		
		for (int i = lowestX; i < highestX; i++){
			for (int j = lowestY; j < highestY; j ++){
				free_room[i][j] = 1;
			}
		}
	}
	

	/**
	 * Given a Tuple, it returns the length from that x and y to the next 1 of the free_room array.
	 * This is useful to know the space we have left in a certain space for the next room
	 * @param initial_tuple
	 * @return
	 */
	public Tuple<Integer, Integer> get_free_room_x_y(Tuple<Integer, Integer> initial_tuple){
		int initial_x = initial_tuple.x + 1;
		int initial_y = initial_tuple.y + 1;
		int final_x = 0;
		int final_y = 0;
		for (int i = initial_x; i < real_x; i++){
			if (free_room[i][initial_y] == 0){
				final_y++;
			} else break;
		}
		for (int j = initial_y; j < real_x; j++){
			if (free_room[initial_x][j] == 0){
				final_x++;
			} else break;
		}
		Tuple<Integer, Integer> free_x_and_y = new Tuple<Integer, Integer>(final_x, final_y);
		return free_x_and_y;
	}
	
	/**
	 * TODO: Finish this function
	 * Given an initial point, it returns the other point where the rooms expands to
	 * should be extended to
	 * @param originalPoint
	 * @return
	 */
	public Tuple<Integer, Integer> nextPoint(Tuple<Integer, Integer> originalPoint, int remainingRooms){
		Tuple<Integer, Integer> nextRoom;
		int leftPoint;
		int rightPoint;
		Tuple<Integer, Integer> freeRoomSpace = get_free_room_x_y(originalPoint);
		int free_room_space_x = freeRoomSpace.x;
		int free_room_space_y = freeRoomSpace.y;
		return null;
	}

	/**
	 * Main function that creates a map given its size, using the rest of the
	 * class functions
	 */
	public void initialize_rooms_map(){
		int number_rooms = 0;
		Tuple<Integer, Integer> nextRoom;
		
		while (number_rooms < size){
			nextRoom = this.obtainAvailableRooms();
			number_rooms++;
		}
	}
	
	
	public Tuple<Integer, Integer> global_init() {
		return global_init;
	}

	public Tuple<Integer, Integer> global_fin() {
		return global_fin;
	}
	
	public void set_global_fin(Tuple<Integer, Integer> tuple) {
		this.global_fin = tuple;
	}
	
	public void set_global_init(Tuple<Integer, Integer> tuple) {
		this.global_init = tuple;
	}
	
	public int get_size() {
		return this.size;
	}
	
	public void set_size(int size) {
		this.size = size;
	}
	
//	public int get_free_room_x() {
//		return this.free_room_x;
//	}
//	
//	public void set_free_room_x(int available_size) {
//		this.free_room_x = available_size;
//	}
//	
//	public int get_free_room_y() {
//		return this.free_room_y;
//	}
//	
//	public void set_free_room_y(int available_size) {
//		this.free_room_y = available_size;
//	}

	public static void main(String[] args) {

	}

}
