package MajorityElementAlgorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MajorityElementMethods {
	public static void main(String[] args){
		String fileName = "Majex1.txt"; //CHANGE FILE NAME TO TRY A DIFFERENT FILE
		int[] allElements = getDataFromFile(fileName);
		System.out.println(allElements.length);
		
		//Method 1: O(N^2)
		findMajorityElementByMethod1(allElements, fileName);
		
		//Method 2: O(N*logN)
		int element = findMajorityElementByMethod2(allElements);
		checkIfElementIsMajority(element, fileName);
		
		//Method 3: O(N)
		findMajorityElementByMethod2(allElements);	
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
	
	public static void findMajorityElementByMethod1(int[] allElements, String fileName) {
		int arrayLength = allElements.length;
		boolean majorityElementExists = false;
		for(int majorityCandidate = 0; majorityCandidate < arrayLength; majorityCandidate++){
			int counter = 0;
			for(int otherNum = 0; otherNum < arrayLength; otherNum++){
				if(allElements[otherNum] == allElements[majorityCandidate])
					counter++;
			}
			if(counter > arrayLength/2){
				majorityElementExists = true;
				System.out.println("Method 1 - " + fileName + ": " + allElements[majorityCandidate] + " is the majority element");
				break;
			}
		}
		if(!majorityElementExists)
			System.out.println("Method 1 - " + fileName + ": No majority element exists");
	}
	
	public static int findMajorityElementByMethod2(int[] allElements) {
		int arrayLength = allElements.length;
		int halfArrayLength = arrayLength/2;
		int elementOfLeft, elementOfRight;
		int[] leftElements = null, rightElements = null;
		
		if(arrayLength == 1){
			return allElements[0];
		}

		if(arrayLength % 2 == 0){
			leftElements = new int[halfArrayLength];
			rightElements = new int[halfArrayLength];
			for(int i = 0; i < halfArrayLength; i++){
				leftElements[i] = allElements[i];
				rightElements[i] = allElements[halfArrayLength + i];
			}
		}else{
			leftElements = new int[halfArrayLength+1];
			rightElements = new int[halfArrayLength];
			for(int i = 0; i <= halfArrayLength; i++){
				leftElements[i] = allElements[i];
				if(i < halfArrayLength)
					rightElements[i] = allElements[halfArrayLength+1 + i];
			}
		}
		elementOfLeft = findMajorityElementByMethod2(leftElements);
		elementOfRight = findMajorityElementByMethod2(rightElements);
		if(elementOfLeft == elementOfRight){
			return elementOfLeft;
		}
		int countOfLeftElement = getFrequency(allElements, elementOfLeft);
		int countOfRightElement = getFrequency(allElements, elementOfRight);
		
		if(countOfLeftElement > halfArrayLength){
			return elementOfLeft;
		}else if(countOfRightElement > halfArrayLength){
			return elementOfRight;
		}else{
			return 0;
		}
	}
	
	//Helper for method2
	public static int getFrequency(int[] allElements, int element) {
		int elementCount = 0;
		for(int i = 0; i < allElements.length; i++){
			if(allElements[i] == element){
				elementCount++;
			}
		}
		return elementCount;
	}
	
	//Checks if element returned from findMajorityElementByMethod2 is majority or not
	public static void checkIfElementIsMajority(int element, String fileName) {
		if(element == 0){
			System.out.println("Method 2 - " + fileName + ": No majority element exists");
		} else{
			System.out.println("Method 2 - " + fileName + ": "+ element + " is the majority element");
		}
	}
}
