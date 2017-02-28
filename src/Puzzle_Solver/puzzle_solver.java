package Puzzle_Solver;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class puzzle_solver{
	
	//Holds the final solved puzzle.
	char[][] solved_puzzle = new char[52][52];
	
	public static void main(String[] args){
		char[][] puzzle;
		String[] words;
		
		puzzle_solver solver = new puzzle_solver();
		puzzle = solver.get_puzzle("puzzleinput.txt");
		words = solver.get_words("wordlist.txt");
		solver.solve_puzzle(puzzle, words);
	}

	//Put each character from puzzleinput.txt into 2D array
	public char[][] get_puzzle(String the_file) {
		char[][] puzzle = new char[52][52];
		
		/*Fill the boundaries of the 2D array by bars '|', so checking boundaries 
		becomes easier while searching for words in the array.*/
		for(int row = 0; row < 52; row++){
			for(int column = 0; column < 52; column++){
				if(row == 0 || row == 51 || column == 0 || column == 51){
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
		
		int puzzle_row = 1;
		while(scan.hasNext()){
			String line = scan.next();
			line = line.toUpperCase();
			char[] char_array = line.toCharArray();
			for(int i = 1; i < 51; i++){
				puzzle[puzzle_row][i] = char_array[i-1];
			}
			puzzle_row++;
		}
		scan.close();
		return puzzle;
	}
	
	//Print the 2D array.
	public void print_multid_array(char[][] multid_array){
		for(int i = 0; i < 52; i++){
			System.out.print("Row " + (i) + ": "); //Delete later
			for(int j = 0; j < 52; j++){
				System.out.print(multid_array[i][j] + " "); //Takeout space
			}
			System.out.print("\n");
		}
	}
	
	public void create_empty_grid(char[][]solved_puzzle){
		for(int i = 0; i < 52; i++){
			for(int j = 0; j < 52; j++){
				solved_puzzle[i][j] = ' ';
			}
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
			word = word.toUpperCase();
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
	
	/*Breaks down each word into char array, and calls a method to find the
	char array in the 2D puzzle*/ 
	public void solve_puzzle(char[][] puzzle, String[] words) {
		print_words(words); //Delete later
		create_empty_grid(solved_puzzle);
		
		Number[] numbers = new Number[20];
		for(int i = 0; i < 20; i++){
			numbers[i] = new Number();
		}
		
		String current_word;
		char[] word_as_chars;
		for(int i = 0; i < 20; i++){
			current_word = words[i];
			word_as_chars = current_word.toCharArray();
			System.out.println("Searching for word- " + current_word + ": ");
			find_word(puzzle, word_as_chars, numbers[i], current_word, 1, 1);
			System.out.println("----------------------------");
		}
		print_multid_array(puzzle); //Delete later
		System.out.println("------------------------------------------------------");
		print_multid_array(solved_puzzle); 
		System.out.println("------------------------------------------------------");
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 20; j++){
				if(is_within(numbers[i], numbers[j])){
					current_word = numbers[i].number;
					word_as_chars = current_word.toCharArray();
					find_word(puzzle, word_as_chars, numbers[i], current_word, numbers[i].start_row, numbers[i].start_col+1);;
					System.out.println("I am being executed");
				}	
			}
		}
		print_multid_array(solved_puzzle); 
	}

	public void find_word(char[][] puzzle, char[] word_as_chars, Number number_info, String current_word, int begin_row, int begin_col) {
		int word_length = word_as_chars.length;
		char[] chars_after_second_pos = Arrays.copyOfRange(word_as_chars, 2, word_length);
		char first_character = word_as_chars[0];
		System.out.println("First letter- " + first_character); //Delete
		System.out.println("Third letter forwards- " + new String(chars_after_second_pos)); //Delete
		boolean found = false;
outer:	for(int row = begin_row; row < 51; row++){
			for(int column = begin_col; column < 51; column++){
				if(puzzle[row][column] == first_character){
					
					//checking North for second character
					if(puzzle[row-1][column] == word_as_chars[1]){
						if(puzzle[row-2][column] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row-1][column]  + ": " + (row-1) + " Column: "+ column);
							System.out.println("Row third char " + puzzle[row-2][column]  + ": " + (row-2) + " Column: "+ column);
						}
						found = check_N(puzzle, chars_after_second_pos, row-1, column, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
					
					//checking North-East for second character
					if(puzzle[row-1][column+1] == word_as_chars[1]){
						if(puzzle[row-2][column+2] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row-1][column+1] + ": " + (row-1) + " Column: "+ (column+1));
							System.out.println("Row third char " + puzzle[row-2][column+2]  + ": " + (row-2) + " Column: "+ (column+2));
						}
						found = check_NE(puzzle, chars_after_second_pos, row-1, column+1, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
					
					//checking East for second character
					if(puzzle[row][column+1] == word_as_chars[1]){
						if(puzzle[row][column+2] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row][column+1] + ": " + (row) + " Column: "+ (column+1));
							System.out.println("Row second char " + puzzle[row][column+2] + ": " + (row) + " Column: "+ (column+2));
						}
						found = check_E(puzzle, chars_after_second_pos, row, column+1, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
					
					//Checking South-East for second character
					if(puzzle[row+1][column+1] == word_as_chars[1]){
						if(puzzle[row+2][column+2] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row+1][column+1] + ": " + (row+1) + " Column: "+ (column+1));
							System.out.println("Row second char " + puzzle[row+2][column+2] + ": " + (row+2) + " Column: "+ (column+2));
						}
						found = check_SE(puzzle, chars_after_second_pos, row+1, column+1, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
					
					//Checking South for second character
					if(puzzle[row+1][column] == word_as_chars[1]){
						if(puzzle[row+2][column] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row+1][column] + ": " + (row+1) + " Column: "+ (column));
							System.out.println("Row second char " + puzzle[row+2][column] + ": " + (row+2) + " Column: "+ (column));
						}
						found = check_S(puzzle, chars_after_second_pos, row+1, column, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
					
					//Checking South-West for second character
					if(puzzle[row+1][column-1] == word_as_chars[1]){
						if(puzzle[row+2][column-2] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row+1][column-1] + ": " + (row+1) + " Column: "+ (column-1));
							System.out.println("Row second char " + puzzle[row+2][column-2] + ": " + (row+2) + " Column: "+ (column-2));
						}
						found = check_SW(puzzle, chars_after_second_pos, row+1, column-1, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
					
					//Checking West for second character
					if(puzzle[row][column-1] == word_as_chars[1]){
						if(puzzle[row][column-2] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row][column-1] + ": " + (row) + " Column: "+ (column-1));
							System.out.println("Row second char " + puzzle[row][column-2] + ": " + (row) + " Column: "+ (column-2));
						}
						found = check_W(puzzle, chars_after_second_pos, row, column-1, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
					
					//Checking North-West for second character
					if(puzzle[row-1][column-1] == word_as_chars[1]){
						if(puzzle[row-2][column-2] == word_as_chars[2]){
							System.out.println("Row first char " + first_character + ": " + row + " Column: "+ column);
							System.out.println("Row second char " + puzzle[row-1][column-1] + ": " + (row-1) + " Column: "+ (column-1));
							System.out.println("Row second char " + puzzle[row-2][column-2] + ": " + (row-2) + " Column: "+ (column-2));
						}
						found = check_NW(puzzle, chars_after_second_pos, row-1, column-1, number_info, current_word);
						if(found == true){
							System.out.println(new String(word_as_chars));
							break outer;
						}
					}
				}
			}
		}
	}
	
	//The following methods check different directions for third characters and more.
	public boolean check_N(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row + 1;
		int original_start_column = column;
		int end_row, end_column;
		
		System.out.println("check_N is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row-(i+1)][column] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in N");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row-(i+1);
					end_column = column;
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					for(int a = original_start_row; a >= end_row; a--){
						solved_puzzle[a][column] = puzzle[a][column];
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean check_NE(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row + 1;
		int original_start_column = column - 1;
		int end_row, end_column;
		
		System.out.println("check_NE is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row-(i+1)][column+(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in NE");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row-(i+1);
					end_column = column+(i+1);
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					int a = original_start_row;
					int b = original_start_column;
					while(a >= end_row && b <= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a--;
						b++;
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean check_E(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row;
		int original_start_column = column - 1;
		int end_row, end_column;
		
		System.out.println("check_E is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row][column+(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in E");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row;
					end_column = column+(i+1);
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					for(int b = original_start_column; b <= end_column; b++){
						solved_puzzle[row][b] = puzzle[row][b];
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean check_SE(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row-1;
		int original_start_column = column - 1;
		int end_row, end_column;
		
		System.out.println("check_SE is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row+(i+1)][column+(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in SE");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row+(i+1);
					end_column = column+(i+1);
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					int a = original_start_row;
					int b = original_start_column;
					while(a <= end_row && b <= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a++;
						b++;
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean check_S(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row-1;
		int original_start_column = column;
		int end_row, end_column;
		
		System.out.println("check_S is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row+(i+1)][column] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in S");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row+(i+1);
					end_column = column;
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					for(int a = original_start_row; a <= end_row; a++){
						solved_puzzle[a][column] = puzzle[a][column];
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean check_SW(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row-1;
		int original_start_column = column+1;
		int end_row, end_column;
		
		System.out.println("check_SW is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row+(i+1)][column-(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in SW");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row+(i+1);
					end_column = column-(i+1);
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					int a = original_start_row;
					int b = original_start_column;
					while(a <= end_row && b >= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a++;
						b--;
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean check_W(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row;
		int original_start_column = column+1;
		int end_row, end_column;
		
		System.out.println("check_W is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row][column-(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in W");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row;
					end_column = column-(i+1);
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					for(int b = original_start_column; b >= end_column; b--){
						solved_puzzle[row][b] = puzzle[row][b];
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean check_NW(char[][] puzzle, char[] chars_after_second_pos, int row, int column, Number number_info, String current_word) {
		
		int original_start_row = row+1;
		int original_start_column = column+1;
		int end_row, end_column;
		
		System.out.println("check_NW is called");
		int remaining_length = chars_after_second_pos.length;
		int counter = 0;
		for(int i = 0; i < remaining_length; i++){
			if(puzzle[row-(i+1)][column-(i+1)] == chars_after_second_pos[i]){
				counter++;
				if(counter == remaining_length){
					System.out.println("Found something in NW");
					System.out.println("Word start row: " + original_start_row);
					System.out.println("Word start col: " + original_start_column);
					end_row = row-(i+1);
					end_column = column-(i+1);
					System.out.println("Word end row: " + end_row);
					System.out.println("Word end col: " + end_column);
					
					int a = original_start_row;
					int b = original_start_column;
					while(a >= end_row && b >= end_column){
						solved_puzzle[a][b] = puzzle[a][b];
						a--;
						b--;
					}
					
					number_info.set_number(current_word, original_start_row, original_start_column, end_row, end_column);
					
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	//Check if f_number is within s_number
	public boolean is_within(Number f_number, Number s_number) {
		if((f_number.start_row == s_number.start_row) &&
		   (f_number.start_col == s_number.start_col) &&
		   (f_number.end_row == s_number.end_row)     &&
		   (f_number.start_col < f_number.end_col)    &&
		   (f_number.end_col < s_number.end_col))
			return true;
		return false;
	}
	
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