package Concordance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BSTConcordance {
	public static void main(String[] args){
		File inputFile = new File("concordinput.txt");
		Scanner scan = null;
		try {
			scan = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println("File " + inputFile + " not found");
			e.printStackTrace();
		}
		
		Node root = null;
		BSTConcordance bst = new BSTConcordance();
		while(scan.hasNext()){
			String word = scan.next();
			if(!isNumber(word)){
				word = word.toUpperCase();
				root = bst.insert(word, root);
			}
		}
		
		bst.inOrderTraversal(root);
	}

	public static boolean isNumber(String word) {
		try{
			double d = Double.parseDouble(word);
		} catch(NumberFormatException nfe){
			return false;
		}
		return true;
	}
	
	private Node insert(String word, Node current){
		if(current == null){
			current = new Node(word);
		}else if(word.compareTo(current.word) > 0){
			current.right = insert(word, current.right);
		}else if(word.compareTo(current.word) < 0){
			current.left = insert(word, current.left);
		}else if(word.equals(current.word)){
			current.frequency++;
		}
		return current;
	}

	public void inOrderTraversal(Node current){
		if(current != null){
			inOrderTraversal(current.left);
			System.out.println(current.word + " (" + current.frequency + ")");
			inOrderTraversal(current.right);
		}
	}
    
	
	private class Node{
		String word;
		int frequency;
		Node left;
		Node right;
		public Node(String theWord){
			word = theWord;
			frequency = 1;
			left = null;
			right = null;
		}
	}
}
