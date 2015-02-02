package util;

import java.util.Arrays;

public class GenericMatrixFunctions {
	
	public static void printMatrix(byte matrix[][]) {
	    for (byte[] row : matrix) 
	        System.out.println(Arrays.toString(row));       
	}
}
