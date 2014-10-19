package map;

import static org.junit.Assert.*;

import org.junit.Test;

import util.Tuple;

public class MapTest {

	Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(100, 100);
	int a = initial_point.x;
	Map map = new Map(initial_point, final_point);
	
	public byte[][] initializeMatrixZero(byte[][] matrix){
		for (int i = 0; i < 100; i++){
			for (int j = 0; j < 100; j++){
				matrix[i][j] = 0;
			}
		}
		return matrix;
	}
	
	public Tuple<Integer, Integer> asdasd(Tuple<Integer, Integer> initial_tuple, byte[][] free_room){
		System.out.println("initial_tuple: " + initial_tuple.x + " " + initial_tuple.y);
		int initial_x = initial_tuple.x + 1;
		int initial_y = initial_tuple.y + 1;
		System.out.println("Free_room[99][99] = " + free_room[99][99]);
//		for (int i = initial_x; i < 105; i++){
//			//System.out.println("i = " + i + "\n" + "initial_y = " + initial_y +  
//				//	"\n" + "final_y = " + final_y + "\n" + "initial_x = " + initial_x + "\n" + 
//					//"real_y = " + 100 + "\n");
//			if (final_x < 105){
//				if (free_room[i][initial_y] == 0){
//					final_x = final_x + 1;
//				} else break;
//			} else break;
//		}
//		for (int j = initial_y; j < 105; j++){
//			if (final_y < 105){
//				if (free_room[initial_x][j] == 0){
//					final_y = final_y + 1;
//				} else break;
//			} else break;
//		}
		for (int i = initial_x; i < 105; i++){
			for (int j = initial_y; j < 105; j++){
				if (free_room[i][j] == 1){
					Tuple<Integer, Integer> free_x_and_y = new Tuple<Integer, Integer>(i - 1, j - 1);
					System.out.println("final_tuple: " + free_x_and_y.x + " " + free_x_and_y.y);
					return free_x_and_y;
				}
			}
		}
		
		return new Tuple<Integer, Integer>(initial_x, initial_y);
		
	}
	
//	@Test
//	public void get_free_room_x_y_test(){
//		byte[][] free_room = new byte[105][105];
//		free_room = this.initializeMatrixZero(free_room);
//		free_room[99][99] = 1;
//		Tuple<Integer, Integer> tuple = new Tuple<Integer, Integer>(55,60);
//		Tuple<Integer, Integer> result = this.asdasd(tuple, free_room);
//		System.out.println("result: " + result.x + " " + result.y);
//	}
	
	@Test
	public void test() {
		System.out.println("before");
		assert(true);
		System.out.println("after");
	}

}
