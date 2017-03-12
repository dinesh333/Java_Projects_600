package MajorityElementAlgorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MajorityElementMethods {
	
	public static int rows;
	public static int columns;
	public static int[][] matrix;
	
	public static void main(String[] args){
		matrix = getDataFromFile("Majex1.txt");
		findMajorityElementByMethod1();
	}

	public static int[][] getDataFromFile(String inputFile){
		//Get numbers of rows and columns in file
		File matrixFile = new File(inputFile);
		Scanner scan = null;
		try{
			scan = new Scanner(matrixFile);
		} catch(FileNotFoundException e) {
			System.out.println("File " + inputFile + " not found");
		}
		
		int total_rows = 0;
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			if(total_rows == 0){	
				String[] numbersInACol = line.split(" ");
				columns = numbersInACol.length;
				System.out.println("Columns in file " + inputFile + " " + numbersInACol.length);
			}
			total_rows++;
		}
		rows = total_rows;
		System.out.println("Rows in file " + inputFile + " " + rows  + "\n");
		scan.close();
		
		//make a matrix and initialize with all 0's
		int[][] matrix = new int[rows][columns];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				matrix[i][j] = 0;
			}
		}
		
		//Get data from file and put in a 2D array
		scan = null;
		try{
			scan = new Scanner(matrixFile);
		} catch(FileNotFoundException e) {
			System.out.println("File " + inputFile + " not found");
		}
		
		int row = 0;
		int cols = columns;
		while(scan.hasNextLine()){
			String line = scan.nextLine();	
			String[] numbersInACol = line.split(" ");
			for(int col = 0; col < cols; col++){
				if(numbersInACol.length == cols){
					int number = Integer.parseInt(numbersInACol[col].toString());
					matrix[row][col] = number;
				} else {
					cols = numbersInACol.length;
					int number = Integer.parseInt(numbersInACol[col].toString());
					matrix[row][col] = number;
				}
			}
			row++;
		}
		
		//Print Matrix
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("");
		}
		scan.close();
		return matrix;
	}
	
	public static void findMajorityElementByMethod1() {
		
	}
}
