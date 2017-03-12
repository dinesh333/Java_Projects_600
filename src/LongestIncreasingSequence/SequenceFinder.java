package LongestIncreasingSequence;

import java.io.*; 
import java.util.*; 

public class SequenceFinder {
	final static int ROWS = 15 + 2; 
	final static int COLUMNS = 15 + 2; 
	final static int[] DIRECTIONS = {-1,0,-1,1,0,1,1,1,1,0,1,-1,0,-1,-1,-1}; //N,NE,E,SE,S,SW,W,NW
	static int[][] paddedMatrix; 
	static ArrayList<Integer> maxLength; 
	
	public static void main(String[] args){
		paddedMatrix = getMatrix("testdataHW3.txt");
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
		
		//To get 2D array from file
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
	
	/*
	 * Get every possible increasing sequence starting from every possible
	 * starting number. If the length of a particular sequence is larger than
	 * the lengths of other sequences before it, assign the sequence to
	 * maxLength.
	 */
	public static void getLongestSequence() {
		maxLength = new ArrayList<>();	
		maxLength.add(1);
		for(int row = 1; row < ROWS-1; row++){
			for(int col = 1; col < COLUMNS-1; col++){
				Stack st = new Stack();
				ArrayList<Integer> startNumAndPos = new ArrayList<>();
				startNumAndPos.add(paddedMatrix[row][col]);
				startNumAndPos.add(row);
				startNumAndPos.add(col);
				findLongestPathFor(startNumAndPos, st);
			}
		}
		
		//Print sequence information of maximum length
		for(int i = 0; i < (maxLength.size()/3); i++){
			int value = i*3;
			int row = value+1;
			int col = value+2;
			value = maxLength.get(value);
			row = maxLength.get(row)-1;
			col = maxLength.get(col)-1;
			System.out.println(value + " (" + row + ", " + col + ")");
		}
		System.out.println("The length of the sequence is: " + (maxLength.size()/3));
	}
	
	public static ArrayList<Integer> findLongestPathFor(ArrayList<Integer> startNumAndPos, Stack st) {
		ArrayList<Integer> surroundingNums = getSurroundingNums(startNumAndPos);
		int startSize = startNumAndPos.size();
		boolean found = false;
		for(int i = 0; i < 24; i++){
			if(i % 3 == 0 && startNumAndPos.get(startSize-3) < surroundingNums.get(i)){
				found = true;
				ArrayList<Integer> sequence = new ArrayList<Integer>();
				sequence.add(surroundingNums.get(i));
				sequence.add(surroundingNums.get(i+1));
				sequence.add(surroundingNums.get(i+2));
				
				ArrayList<Integer> newList = new ArrayList<Integer>();
				newList.addAll(startNumAndPos);
				newList.addAll(sequence);
				
				st.push(newList);
			}
		}
		
		if(!st.empty()){
			ArrayList<Integer> top = (ArrayList<Integer>) st.pop();
			if(top.size() > maxLength.size())
				maxLength = top;
			return findLongestPathFor(top, st);
		}
		return startNumAndPos;
	}
	
	//Returns ArrayList of surrounding numbers with their positions: 8 + 2 * 8 = 24 total elements returned
	public static ArrayList<Integer> getSurroundingNums(ArrayList<Integer> startNumAndPos) {
		int startSize = startNumAndPos.size();
		int row = startNumAndPos.get(startSize-2);
		int col = startNumAndPos.get(startSize-1);
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

