package map;

import java.util.ArrayList;

import util.RandUtil;
import util.Tuple;

public class Map {
	
	private ArrayList<Room> map;
	private Tuple<Integer, Integer> global_init;
	private Tuple<Integer, Integer> global_fin;
	private int size;

	/**
	 * Creates a random map, which is a collection of rooms
	 * @param global_x
	 * @param global_y
	 */
	
	public Map(Tuple<Integer, Integer> global_init, Tuple<Integer, Integer> global_fin){
		this.set_global_init(global_init);
		this.set_global_init(global_fin);
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
)	 * Given the number of rooms and the dimension of the map, it sets
	 * the map with its rooms
	 * @return
	 */
	public ArrayList<Room> obtainRooms(){
		int number_rooms = obtainNumberRooms();
		int real_x = this.global_fin().x - this.global_init().x;
		int real_y = this.global_fin().y - this.global_init().y;
		byte[][] free_room = new byte[real_x][real_y]; 
		initializeMatrixZero(free_room);
		
		while (number_rooms != 0){
			
		}

		return new ArrayList<Room>();
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
