package map;

import static org.junit.Assert.*;

import org.junit.Test;

import util.GenericMatrixFunctions;
import util.Tuple;

public class MapTest {

	Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(10, 10);
	int a = initial_point.x;
	Map map = new Map(initial_point, final_point);
	
	@Test
	public void testMap(){
		GenericMatrixFunctions.printMatrix(map.getFreeRoom());
		for (Room r: map.getRooms()){
			System.out.println("initial: (" + r.getGlobal_initial().x + "," + r.getGlobal_initial().y + ")");
			System.out.println("final: (" + r.getGlobal_final().x + "," + r.getGlobal_final().y + ")");
		}
		assertTrue(true);
	}

}
