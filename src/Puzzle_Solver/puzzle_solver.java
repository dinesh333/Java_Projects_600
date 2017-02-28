package Puzzle_Solver;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.*;

public class puzzle_solver{
	//2 extra rows and columns to help with checking in the boundaries.
	final static int puzzle_rows = 52; 
	final static int puzzle_cols = 52; 
	final static int puzzle_words = 20;
	
	public static void main(String[] args){
		long start_time, elapsed_time;
		start_time = System.nanoTime();
		
		char[][] puzzle, solved_puzzle;
		String[] words;
		//Holds the final solved puzzle for neatly printing on screen.
		solved_puzzle = new char[puzzle_rows][puzzle_cols];
		
		puzzle_solver solver = new puzzle_solver();
		puzzle = solver.get_puzzle("puzzleinput.txt");
		words = solver.get_words("wordlist.txt");
		solver.create_empty_grid(solved_puzzle);
		solver.solve_puzzle(puzzle, words, solved_puzzle);
		
		elapsed_time = System.nanoTime() - start_time;
		System.out.println("\nRunning time: " + (elapsed_time * (Math.pow(10, -6))) + " ms");
	}

	//Put each character from puzzleinput.txt into 2D array
	public char[][] get_puzzle(String the_file) {
		char[][] puzzle = new char[puzzle_rows][puzzle_cols];
		
		/*Fill rows 0 and 51, and columns 0 and 51 of the 2D array by bars '|', 
		so checking boundaries becomes easier while searching for words in the array.*/
		for(int row = 0; row < puzzle_rows; row++){
			for(int column = 0; column < puzzle_cols; column++){
				if(row == 0 || row == (puzzle_rows-1) || column == 0 || column == (puzzle_rows-1)){
					puzzle[row][column] = '|';
				}
			}
		}
		
		File input_file = new File(the_file);
		Scanner scan = null;
		try {
			scan = new Scanner(input_file);
		} catch (FileNotFoundException e) {
			System.out.println("File " + the_file + " not found");
		}
		
		//Fill up puzzle rows 1 to 50 and columns 1 to 50 with characters from file.
		int row = 1;
		while(scan.hasNext()){
			String line = scan.next();
			line = line.toUpperCase();
			char[] char_array = line.toCharArray();
			for(int i = 1; i < puzzle_rows-1; i++){
				puzzle[row][i] = char_array[i-1];
			}
			row++;
		}
		scan.close();
		return puzzle;
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
			word = word.toUpperCase();
			words[index] = word;
			index++;
		}
		
		scan.close();
		return words;
	}
	
	/*Create an empty array of size 52 x 52 so it can be filled with
	words found in the puzzle later on.*/
	public void create_empty_grid(char[][]solved_puzzle){
		for(int i = 0; i < 52; i++){
			for(int j = 0; j < 52; j++){
				solved_puzzle[i][j] = ' ';
			}
		}
	}
	
	/*Method for solving puzzle, and printing the result*/
	public void solve_puzzle(char[][] puzzle, String[] words, char[][] solved_puzzle) {
		/*Create an inner class object for each word in the puzzle so
		the number, and its information such as start row, start column, 
		end row, end column can be saved in the object for later use*/
		Number[] numbers = new Number[puzzle_words];
		for(int i = 0; i < puzzle_words; i++){
			numbers[i] = new Number();
		}
		
		//Find each word in the puzzle, and if found, save in solved_puzzle.
		String current_word;
		for(int i = 0; i < puzzle_words; i++){
			current_word = words[i];
			find_word(puzzle, current_word, numbers[i], 1, 1, solved_puzzle);
		}
	
		char[] word_as_chars;
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 20; j++){
				/*Checking if the first word falls within second word in the East side only.
				Without this code, FOUR can't be found separately, and will be found within FOURTEEN.*/
				if(is_within(numbers[i], numbers[j])){
					current_word = numbers[i].number;
					word_as_chars = current_word.toCharArray();
					/*First word falls within second word in the East side: start searching 
					for the first word from the same row but next column*/
					find_word(puzzle, current_word, numbers[i], numbers[i].start_row, numbers[i].start_col+1, solved_puzzle);
				}	
			}
		}
		print_multid_array(solved_puzzle); 
	}
	
	//Print the solved 2D array.
	public void print_multid_array(char[][] multid_array){
		for(int i = 1; i < puzzle_rows-1; i++){
			System.out.print("Row " + (i) + ": ");
			for(int j = 1; j < puzzle_cols-1; j++){
				System.out.print(multid_array[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	//Find a word in the puzzle starting at begin_row and begin_col. 
	public void find_word(char[][] puzzle, String current_word, Number number_info, int begin_row, int begin_col, char[][] solved_puzzle) {
		char[] word_as_chars = current_word.toCharArray();
		char first_character = word_as_chars[0];
		boolean found = false;
outer:	for(int row = begin_row; row < puzzle_rows-1; row++){
			for(int column = begin_col; column < puzzle_cols-1; column++){
				if(puzzle[row][column] == first_character){
					
					//checking North for second character
					if(puzzle[row-1][column] == word_as_chars[1]){
						found = check_N(puzzle, row-1, column, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
					
					//checking North-East for second character
					if(puzzle[row-1][column+1] == word_as_chars[1]){
						found = check_NE(puzzle, row-1, column+1, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
					
					//checking East for second character
					if(puzzle[row][column+1] == word_as_chars[1]){
						found = check_E(puzzle, row, column+1, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
					
					//Checking South-East for second character
					if(puzzle[row+1][column+1] == word_as_chars[1]){
						found = check_SE(puzzle, row+1, column+1, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
					
					//Checking South for second character
					if(puzzle[row+1][column] == word_as_chars[1]){
						found = check_S(puzzle, row+1, column, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
					
					//Checking South-West for second character
					if(puzzle[row+1][column-1] == word_as_chars[1]){
						found = check_SW(puzzle, row+1, column-1, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
					
					//Checking West for second character
					if(puzzle[row][column-1] == word_as_chars[1]){
						found = check_W(puzzle, row, column-1, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
					
					//Checking North-West for second character
					if(puzzle[row-1][column-1] == word_as_chars[1]){
						found = check_NW(puzzle, row-1, column-1, number_info, current_word, solved_puzzle);
						if(found == true)
							break outer;
					}
				}
			}
		}
	}
	
/********************************************************************************************************************
			The following methods check different directions for third characters and more.
*********************************************************************************************************************/
	
	public boolean check_N(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row + 1;
		original_start_column = column;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row-(i+1)][column] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row-(i+1);
					end_column = column;				
					for(int a = original_start_row; a >= end_row; a--){
						solved_puzzle[a][column] = puzzle[a][column];
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
	
	public boolean check_NE(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row + 1;
		original_start_column = column - 1;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row-(i+1)][column+(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row-(i+1);
					end_column = column+(i+1);
					int a = original_start_row;
					int b = original_start_column;
					while(a >= end_row && b <= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a--;
						b++;
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
	
	public boolean check_E(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row;
		original_start_column = column - 1;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row][column+(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row;
					end_column = column+(i+1);
					for(int b = original_start_column; b <= end_column; b++){
						solved_puzzle[row][b] = puzzle[row][b];
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
	
	public boolean check_SE(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row-1;
		original_start_column = column - 1;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row+(i+1)][column+(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row+(i+1);
					end_column = column+(i+1);
					int a = original_start_row;
					int b = original_start_column;
					while(a <= end_row && b <= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a++;
						b++;
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
	
	public boolean check_S(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row-1;
		original_start_column = column;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row+(i+1)][column] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row+(i+1);
					end_column = column;
					for(int a = original_start_row; a <= end_row; a++){
						solved_puzzle[a][column] = puzzle[a][column];
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
	
	public boolean check_SW(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row-1;
		original_start_column = column+1;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row+(i+1)][column-(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row+(i+1);
					end_column = column-(i+1);
					int a = original_start_row;
					int b = original_start_column;
					while(a <= end_row && b >= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a++;
						b--;
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
	
	public boolean check_W(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row;
		original_start_column = column+1;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row][column-(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row;
					end_column = column-(i+1);
					for(int b = original_start_column; b >= end_column; b--){
						solved_puzzle[row][b] = puzzle[row][b];
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
	
	public boolean check_NW(char[][] puzzle, int row, int column, Number number_info, String current_word, char[][]solved_puzzle) {
		int end_row, end_column, original_start_row, original_start_column, word_length, remaining_length, counter;
		char[] word_as_chars, chars_after_second_pos;
		
		original_start_row = row+1;
		original_start_column = column+1;
		word_as_chars = current_word.toCharArray();
		word_length = word_as_chars.length;
		chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		remaining_length = chars_after_second_pos.length;
		counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row-(i+1)][column-(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					//Word found. Transfer word from original puzzle to solved_puzzle.
					end_row = row-(i+1);
					end_column = column-(i+1);
					int a = original_start_row;
					int b = original_start_column;
					while(a >= end_row && b >= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a--;
						b--;
					}
					//save information about word found in a Number object.
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					return true;
				}
			}else
				return false;
		}
		return false;
	}
/*******************************************************************************************************/
	
	//Check if f_number is within s_number in the East side.
	public boolean is_within(Number f_number, Number s_number) {
		if((f_number.start_row == s_number.start_row) &&
		   (f_number.start_col == s_number.start_col) &&
		   (f_number.end_row == s_number.end_row)     &&
		   (f_number.start_col < f_number.end_col)    &&
		   (f_number.end_col < s_number.end_col))
			return true;
		return false;
	}
	
	//Number class to save information about a word.
	private class Number{
		String number;
		int start_row, start_col, end_row, end_col;
		
		public void set_number(String num, int s_row, int s_col, int e_row, int e_col){
			number = num;
			start_row = s_row;
			start_col = s_col;
			end_row = e_row;
			end_col = e_col;
		}
	}
}