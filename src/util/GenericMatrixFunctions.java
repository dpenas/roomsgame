package util;

public class GenericMatrixFunctions {
	
	public static void printMatrix(byte[][] matrix, int x, int y){
		for (int i = 0; i < x; i++){
			for (int j = 0; j < y; j++){
				System.out.println(matrix[i][j]);
			}
		}
	}
}
