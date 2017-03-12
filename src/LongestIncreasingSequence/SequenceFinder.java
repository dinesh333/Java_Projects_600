package LongestIncreasingSequence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class SequenceFinder {
	
	final static int ROWS = 4 + 2; //+2 for 2 extra rows of 0s.
	final static int COLUMNS = 4 + 2; //+2 for 2 extra columns of 0s.
	// x and y coordinates for 8 directions, starting from North
	static int[][] paddedMatrix;
	
	public static void main(String[] args){
		paddedMatrix = getMatrix("sampledata.txt");
		//printMatrix(paddedMatrix);
		getLongestSequence();	
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
	
	public static void printMatrix(ArrayList<Integer>[][] matrix) {
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++){
				System.out.print(String.format("%1$6s", matrix[i][j]));
			}
			System.out.println("");
		}
	}
	
	/*
	 * For each number in matrix, and find out longest sequence if you start
	 * from that number.
	 */
	public static void getLongestSequence() {
		ArrayList<Integer>[][] longestPaths = new ArrayList[ROWS][COLUMNS]; 
		for(int row = 1; row < ROWS-1; row++){
			for(int col = 1; col < COLUMNS-1; col++){
				Stack st = new Stack();
				ArrayList<Integer> startNumAndPos = new ArrayList<>();
				startNumAndPos.add(paddedMatrix[row][col]);
				startNumAndPos.add(row);
				startNumAndPos.add(col);
				longestPaths[row][col] = findLongestPathFor(startNumAndPos, st);
			}
		}
		printMatrix(longestPaths);
	}
	
	public static ArrayList<Integer> findLongestPathFor(ArrayList<Integer> startNumAndPos, Stack st) {
		System.out.println(startNumAndPos); //Delete later
		ArrayList<Integer> surroundingNums = getSurroundingNums(startNumAndPos); 
		
		for(int i = 0; i < 24; i++){
			if(i % 3 == 0 && startNumAndPos.get(0) < surroundingNums.get(i)){
				ArrayList<Integer> sequence = startNumAndPos;
				sequence.add(surroundingNums.get(i));
				sequence.add(surroundingNums.get(i+1));
				sequence.add(surroundingNums.get(i+2));
				st.push(sequence);
				System.out.println(surroundingNums); //Delete later
			}
		}
		System.out.println("------------------------------------------------");
		
		if(st.empty()){ //base case
			return startNumAndPos;
		} else { //recursive case
			return startNumAndPos; //will be changed
		}
	}
	
	//Returns ArrayList of surrounding numbers with their positions: 8 + 2 * 8 = 24 total elements
	private static ArrayList<Integer> getSurroundingNums(ArrayList<Integer> startNumAndPos) {
		int row = startNumAndPos.get(1);
		int col = startNumAndPos.get(2);
		ArrayList<Integer> allSurroundingNums = new ArrayList<>();
		for(int i = 0; i <= 7; i++){
			int new_row = row + (DIRECTIONS[2*i]);
			int new_col = col + (DIRECTIONS[2*i+1]);
			allSurroundingNums.add(paddedMatrix[new_row][new_col]);
			allSurroundingNums.add(new_row);
			allSurroundingNums.add(new_col);
		}
		return allSurroundingNums;
	}
}

