package map;

import java.util.ArrayList;

import util.GenericMatrixFunctions;
import util.RandUtil;
import util.Tuple;

/**
 * TODO RELEVANT:
 * - Create the doors algorithm between the rooms (should it be here or the room itself) -> Probably here
 * - Test
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
	boolean is_there_free_space = true;
	private ArrayList<Room> rooms;
	private int size;

	/**
	 * Creates a random map, which is a collection of rooms
	 * @param global_x
	 * @param global_y
	 */
	
	public Map(Tuple<Integer, Integer> global_init, Tuple<Integer, Integer> global_fin){
		this.set_global_init(global_init);
		this.set_global_fin(global_fin);
		real_x = this.global_fin().x - this.global_init().x;
		real_y = this.global_fin().y - this.global_init().y;
		size = real_x * real_y;
		free_room = new byte[real_x][real_y];
		initializeMatrixZero(free_room);
		rooms = new ArrayList<Room>();
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
		if (min_num == max_num) return min_num;
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
	public Tuple<Integer, Integer> obtainAvailableRoom(){
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
				} else {
					if (j == 0 && i != 0){
						if (free_room[i][0] == 0 && free_room[i-1][0] == 1){
							possibleExtensionPoints.add(new Tuple<Integer, Integer>(i-1, 0));
						}
					} else {
						if (j != 0 && i != 0 && (free_room[i-1][j-1] == 1 && free_room[i][j] == 0)){
							possibleExtensionPoints.add(new Tuple<Integer, Integer>(i-1, j-1));
						}
					}
				}
			}
		}
		
		possibleExtensionPoints = cleanArray(possibleExtensionPoints);
		
		for(int i = 0; i < possibleExtensionPoints.size(); i++) {
		    System.out.print("Posible punto: " + possibleExtensionPoints.get(i).x + " " + possibleExtensionPoints.get(i).y + "\n");
		}
		
		int position_random_selected = RandUtil.RandomNumber(0, possibleExtensionPoints.size());
		
		return possibleExtensionPoints.get(position_random_selected);

	}
	
	/**
	 * Returns a tuple without the useless positions of the map. We want to get the
	 * positions where either i or j are 0 and then the corners; the rest of the positions
	 * are not interesting for the map generation since it would create very small rooms
	 * @param tuple1
	 * @return
	 */
	public ArrayList<Tuple<Integer, Integer>> cleanArray(ArrayList<Tuple<Integer, Integer>> arrayTuple){
		ArrayList<Tuple<Integer, Integer>> finalArrayList = new ArrayList<>();
		Tuple<Integer, Integer> cornerTuple = null; // Tuple in the corner
		Tuple<Integer, Integer> leftTuple = null; // Tuple in the left
		Tuple<Integer, Integer> rightTuple = null; // Tuple in the right
		for (int i = 0; i < arrayTuple.size(); i++){
			Tuple<Integer, Integer> actualTuple = arrayTuple.get(i);
			if (i == 0){
				leftTuple = arrayTuple.get(i);
				cornerTuple = arrayTuple.get(i);
				rightTuple = arrayTuple.get(i);
			} else{
				// If it is the point most on the left
				if (actualTuple.x < leftTuple.x){
					leftTuple = actualTuple;
				}
				// If it is the point most of the right and down
				if (actualTuple.y >= cornerTuple.y && actualTuple.x > cornerTuple.x){
					cornerTuple = actualTuple;
				}
				
				if(actualTuple.y <= rightTuple.y && actualTuple.x > rightTuple.x){
					rightTuple = actualTuple;
				}
			}
		}
		
		// We check if there are tuples that are the same before
		// putting them into the array
		finalArrayList.add(leftTuple);
		if (leftTuple.x != rightTuple.x || leftTuple.y != rightTuple.y){
			finalArrayList.add(rightTuple);
		}
		if (leftTuple.x != cornerTuple.x || leftTuple.y != cornerTuple.y){
			if (rightTuple.x != cornerTuple.x || rightTuple.y != cornerTuple.y){
				finalArrayList.add(cornerTuple);
			}
		}
		
		return finalArrayList;
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
			//System.out.println("Entro en la primera: highestY " + highestY + "\n");
			//System.out.println("Entro en la primera: lowestY " + lowestY + "\n");
		} else {
			//System.out.println("Entro en la segunda " + tuple1.y + "\n");
			lowestY = tuple2.y;
			highestY = tuple1.y;
		}
		System.out.println("lowestY: " + lowestY + "\n");
		System.out.println("lowestX: " + lowestX + "\n");
		System.out.println("highestX: " + highestX + "\n");
		System.out.println("highestY: " + highestY + "\n");
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
		// System.out.println("initial_tuple: " + initial_tuple.x + " " + initial_tuple.y);
		int initial_x = initial_tuple.x + 1;
		int initial_y = initial_tuple.y + 1;
		for (int i = initial_x; i < real_x; i++){
			for (int j = initial_y; j < real_y; j++){
				if (free_room[i][j] == 1){
					Tuple<Integer, Integer> free_x_and_y = new Tuple<Integer, Integer>(i - initial_x - 1, j - initial_y - 1);
					// System.out.println("final_tuple!!!!!: " + free_x_and_y.x + " " + free_x_and_y.y);
					return free_x_and_y;
				}
			}
		}
		System.out.println("ENTRO AQUÍ\n");
		return new Tuple<Integer, Integer>(real_x, real_y);
	}
	
	/**
	 * Given an initial point, it returns the other point where the rooms should be extended to
	 * @param originalPoint
	 * @return
	 */
	public Tuple<Integer, Integer> nextPoint(Tuple<Integer, Integer> originalPoint, int remainingRooms){

		Tuple<Integer, Integer> freeRoomSpace = get_free_room_x_y(originalPoint);
		int free_room_space_x = freeRoomSpace.x;
		int free_room_space_y = freeRoomSpace.y;
		if (remainingRooms == 1){
			// If there's only one room left, then we cover all the space
			Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(free_room_space_x, free_room_space_y);
			return nextRoom;
		} else{
			double possible_real_x = free_room_space_x/remainingRooms;
			double possible_real_y = free_room_space_y/remainingRooms;
			int possible_real_x_low = (int) Math.floor(possible_real_x);
			int possible_real_x_high = (int) Math.ceil(possible_real_x);
			int possible_real_y_low = (int) Math.floor(possible_real_y);
			int possible_real_y_high = (int) Math.ceil(possible_real_y);
			int rand_number = RandUtil.RandomNumber(0, 1);
			if (rand_number == 0){
				int actual_x = originalPoint.x + possible_real_x_low;
				int actual_y = originalPoint.y + possible_real_y_low;
				Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(actual_x, actual_y);
				return nextRoom;
			} else {
				int actual_x = originalPoint.x + possible_real_x_high;
				int actual_y = originalPoint.y + possible_real_y_high;
				Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(actual_x, actual_y);
				return nextRoom;
			}
		}
	}
	
	/**
	 * Sets the is_there_free_space boolean value to true or false depending on 
	 * the matrix free_room. If it still has space in it (0 values), then it is
	 * true, if not; it is false.
	 * 
	 */
	public void check_free_space(){
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				if (free_room[i][j] == 0){
					this.is_there_free_space = true;
					return;
				}
			}
		}
		this.is_there_free_space = false;
	}
	
	/**
	 * It is possible that after initializing a room there are some spaces without
	 * rooms, so this function will create rooms in those free spaces
	 */
	public void complete_map(){
		Tuple<Integer, Integer> initialPoint;
		Tuple<Integer, Integer> finalPoint;
		while (is_there_free_space){
			for (int i = 0; i < real_x; i++){
				for (int j = 0; j < real_y; j++){
					if (free_room[i][j] == 0){
						initialPoint = new Tuple<Integer, Integer>(i, j);
						finalPoint = nextPoint(initialPoint, 1);
						System.out.println("Punto 49 44: " + free_room[80][80] + "\n");
//						System.out.println("InitialPoint: " + initialPoint.x + " " + initialPoint.y + "\n");
//						System.out.println("FinalPoint: " + finalPoint.x + " " + finalPoint.y + "\n");
						createRoomMatrix(initialPoint, finalPoint);
						Room r = new Room(initialPoint, finalPoint);
						this.rooms.add(r);
					}
				}
			}
			check_free_space();
		}
	}
	
	/**
	 * Main function that creates a map given its size, using the rest of the
	 * class functions
	 */
	public void initialize_rooms_map(){
		int number_rooms = 0;
		Tuple<Integer, Integer> initialPoint;
		Tuple<Integer, Integer> finalPoint;
		// int total_number_rooms = this.obtainNumberRooms();
		int total_number_rooms = 2;
		
		while (number_rooms < total_number_rooms){
			initialPoint = this.obtainAvailableRoom();
			finalPoint = nextPoint(initialPoint, total_number_rooms - number_rooms);
			System.out.println("InitialPoint HOLA: " + initialPoint.x + " " + initialPoint.y + "\n");
			System.out.println("finalPoint HOLA: " + finalPoint.x + " " + finalPoint.y + "\n");
			Room r = new Room(initialPoint, finalPoint);
			createRoomMatrix(initialPoint, finalPoint);
			System.out.println("Free room: " + free_room[49][49] + "\n");
			this.rooms.add(r);
			number_rooms++;
		}
		complete_map();
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

}
