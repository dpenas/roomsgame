package map;

import java.util.ArrayList;

import util.RandUtil;
import util.Tuple;

/**
 * TODO RELEVANT:
 * - Complete initialize_rooms_map, which should call the other functions and create the actual map
ß * 
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
	private int size;

	/**
	 * Creates a random map, which is a collection of rooms
	 * @param global_x
	 * @param global_y
	 */
	
	public Map(Tuple<Integer, Integer> global_init, Tuple<Integer, Integer> global_fin){
		this.set_global_init(global_init);
		this.set_global_init(global_fin);
		real_x = this.global_fin().x - this.global_init().x;
		real_y = this.global_fin().y - this.global_init().y;
		size = real_x * real_y;
		free_room = new byte[real_x][real_y];
		initializeMatrixZero(free_room);
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
	 * @return Position of the next room that we must extend
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
	 * Main function that creates a map given its size, using the rest of the
	 * class functions
	 */
	public void initialize_rooms_map(){
		
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

	public static void main(String[] args) {

	}

}
