package MajorityElementAlgorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MajorityElementMethods {
	public static void main(String[] args){
		int[] allElements = getDataFromFile("Majex1.txt");
		System.out.println(allElements.length);
		findMajorityElementByMethod1(allElements);
	}

	public static int[] getDataFromFile(String inputFile){
		File matrixFile = new File(inputFile);
		Scanner scan = null;
		try{
			scan = new Scanner(matrixFile);
		} catch(FileNotFoundException e) {
			System.out.println("File " + inputFile + " not found");
		}

		ArrayList<Integer> numbers = new ArrayList<Integer>();
		while(scan.hasNextInt()){
			Integer i = scan.nextInt();
			numbers.add(i);
		}
		
		int[] allElements = new int[numbers.size()];
		for(int i = 0; i < allElements.length;i++){
			allElements[i] = numbers.get(i);
		}
		scan.close();
		return allElements;
	}
	
	public static void findMajorityElementByMethod1(int[] allElements) {
		int arrayLength = allElements.length;
		boolean majorityElementExists = false;
		for(int majorityCandidate = 0; majorityCandidate < arrayLength; majorityCandidate++){
			int counter = 0;
			for(int otherNum = 0; otherNum < arrayLength; otherNum++){
				if(allElements[otherNum] == allElements[majorityCandidate])
					counter++;
			}
			if(counter >= arrayLength/2){
				majorityElementExists = true;
				System.out.println(allElements[majorityCandidate] + " is the majority element");
				break;
			}
		}
		if(!majorityElementExists)
			System.out.println("No majority element exists");
	}
}
