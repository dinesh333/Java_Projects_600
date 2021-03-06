package MajorityElementAlgorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MajorityElementMethods {
	public static void main(String[] args){
		long startTime, stopTime;
		
		String fileName = "Majex4.txt"; //CHANGE FILE NAME TO TRY A DIFFERENT FILE
		int[] allElements = getDataFromFile(fileName);
		
		//Method 1: O(N^2)
		startTime = System.nanoTime();
		findMajorityElementByMethod1(allElements, fileName);
		stopTime = System.nanoTime();
		getRunningTime(startTime, stopTime);
		
		//Method 2: O(N*logN)
		startTime = System.nanoTime();
		int element = findMajorityElementByMethod2(allElements);
		checkIfElementIsMajority(element, fileName);
		stopTime = System.nanoTime();
		getRunningTime(startTime, stopTime);
		
		//Method 3: O(N)
		startTime = System.nanoTime();
		int majorityIndex = findMajorityElementByMethod3(allElements, fileName);
		checkIfIndexHasMajority(allElements, fileName, majorityIndex);
		stopTime = System.nanoTime();
		getRunningTime(startTime, stopTime);
	}
	
	public static void getRunningTime(long startTime, long stopTime){
		long elapsedTime = stopTime - startTime;
		System.out.println("Running time: " + (elapsedTime * (Math.pow(10, -6))) + " ms\n");
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
	private static int getFrequency(int[] allElements, int element) {
		int elementCount = 0;
		for(int i = 0; i < allElements.length; i++){
			if(allElements[i] == element){
				elementCount++;
			}
		}
		return elementCount;
	}
	
	//Checks if element returned from findMajorityElementByMethod2 is majority or not
	private static void checkIfElementIsMajority(int element, String fileName) {
		if(element == 0){
			System.out.println("Method 2 - " + fileName + ": No majority element exists");
		} else{
			System.out.println("Method 2 - " + fileName + ": "+ element + " is the majority element");
		}
	}
	
	//Method 3 - step 1: find the candidate for majority index
	public static int findMajorityElementByMethod3(int[] allElements, String fileName) {
		int majority_index = 0, count = 1;
		for(int i = 1; i < allElements.length; i++){
			if(allElements[majority_index] == allElements[i]){
				count++;
			} else {
				count--;
				if(count == 0){
					majority_index = i;
					count = 1;
				}
			}
		}
		return majority_index;
	}
	
	//Method 3 - step 1: check if the candidate at an index is the majority element 
	private static void checkIfIndexHasMajority(int[] allElements, String fileName, int majorityIndexCandidate) {
		int count = 0;
		for(int i = 0; i < allElements.length; i++){
			if(allElements[i] == allElements[majorityIndexCandidate]){
				count++;
			}
		}
		
		if(count > (allElements.length/2)){
			System.out.println("Method 3 - " + fileName + ": "+ allElements[majorityIndexCandidate] + " is the majority element");
		} else{
			System.out.println("Method 3 - " + fileName + ": No majority element exists");
		}
	}
}
