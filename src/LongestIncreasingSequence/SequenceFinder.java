package LongestIncreasingSequence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SequenceFinder {
	
	final static int ROWS = 4 + 2; //+2 for 2 extra rows of 0s.
	final static int COLUMNS = 4 + 2; //+2 for 2 extra columns of 0s.
	static int[][] padded_matrix;
	
	public static void main(String[] args){
		padded_matrix = getMatrix("sampledata.txt");
		printMatrix(padded_matrix);
		
	}

	public static int[][] getMatrix(String fileWithMatrix){
		int[][] matrix = new int[ROWS][COLUMNS];
		
		//Filling boundaries with 0, so boundary-checking becomes easier
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++){
				if(i == 0 || i == (ROWS - 1) || j == 0 || j == (COLUMNS-1)){
					matrix[i][j] = 0;
				}
			}
		}
		
		File matrixFile = new File(fileWithMatrix);
		Scanner scan = null;
		
		try{
			scan = new Scanner(matrixFile);
		} catch(FileNotFoundException e) {
			System.out.println("File " + fileWithMatrix + " not found");
		}
		
		int row = 1;
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			String[] number = line.split(",");
			for(int col = 1; col < COLUMNS-1; col++){
					matrix[row][col] = Integer.parseInt(number[col-1].trim()); 
			}
			row++;
		}
		return matrix;
	}
	
	public static void printMatrix(int[][] matrix) {
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++){
				System.out.print(String.format("%1$4s", matrix[i][j]));
			}
			System.out.println("");
		}
	}
}

