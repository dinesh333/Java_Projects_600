package Puzzle_Solver;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class puzzle_solver{
	public static void main(String[] args){
		char[][] puzzle;
		String[] words;
		
		puzzle_solver solver = new puzzle_solver();
		
		puzzle = solver.get_puzzle("puzzleinput.txt");
		solver.print_multid_array(puzzle); //Delete later
		
		words = solver.get_words("wordlist.txt");
		solver.print_words(words); //Delete later
		
		//SO FAR: IMPORTED THE PUZZLE AS 2D ARRAY IN puzzle 2D array
		//ALSO ALL THE WORDS FROM One to Twenty ARE SAVED IN words 1D array.
	}
	
	//Put each character from puzzleinput.txt into 2D array
	public char[][] get_puzzle(String the_file) {
		char[][] puzzle = new char[50][50];
		File input_file = new File(the_file);
		Scanner scan = null;
		try {
			scan = new Scanner(input_file);
		} catch (FileNotFoundException e) {
			System.out.println("File " + the_file + " not found");
		}
		
		int puzzle_row = 0;
		while(scan.hasNext()){
			String line = scan.next();
			char[] char_array = line.toCharArray();
			for(int i = 0; i < 50; i++){
				puzzle[puzzle_row][i] = char_array[i];
			}
			puzzle_row++;
		}
		scan.close();
		return puzzle;
	}
	
	//Print the 2D array.
	public void print_multid_array(char[][] multid_array){
		for(int i = 0; i < 50; i++){
			System.out.print("Row " + (i+1) + ": "); //Delete later
			for(int j = 0; j < 50; j++){
				System.out.print(multid_array[i][j] + " "); //Takeout space
			}
			System.out.print("\n");
		}
	}
	
	//Get all the words from wordlist.txt to search for in puzzle
	public String[] get_words(String the_file){
		String[] words = new String[20];
		File input_file = new File(the_file);
		Scanner scan = null;
		try{
			scan = new Scanner(input_file);
		} catch (FileNotFoundException e){
			System.out.println("File " + the_file + " not found");
		}
		
		int index = 0;
		while(scan.hasNext()){
			String word = scan.next();
			words[index] = word;
			index++;
		}
		
		scan.close();
		return words;
	}
	
	//Print the words to search for in puzzle. Delete later.
	public void print_words(String[] words){
		for(int i = 0; i < 20; i++){
			System.out.println(words[i]);
		}
	}
}