package MaximumSubSequenceAlgorithms;

import java.util.Random;

public class MaxSubSequence {
	final static int MAX = 1000;
	final static int MIN = -1000;
	
	public static void main(String[] args){
		int arraySize;
		int[] array;
		
		arraySize = 5000; //CHANGE THE ARRAY SIZE TO SEE THE EXECUTION TIMES FOR DIFFERENT N VALUES.
		array = getArrayOfSize(arraySize);
		find4RunningTimes(array, arraySize);
	}
	
	/**
	 * Find maximum subsequence of an array by using 4 different algorithms.
	 * @param array = array to find maximum subsequence of 
	 * @param arraySize = size of the array
	 */
	public static void find4RunningTimes(int[] array, int arraySize) {
		long startTime, stopTime, elapsedTime;
		int sum = 0;
		System.out.println("N = " + arraySize + "\n\nPlease wait. Calculating sum of "
				+ "maximum subsequence and running times\n" );
		for(int algorithm = 1; algorithm <= 4; algorithm++){
			startTime = System.nanoTime();
			if (algorithm == 1){
				sum = maxSubSum1(array);
				System.out.println("Sum = " + sum + "\n");
			}
			else if (algorithm == 2)
				sum = maxSubSum2(array);
			else if (algorithm == 3)
				sum= maxSubSum3(array);
			else if (algorithm == 4)
				sum = maxSubSum4(array);
			stopTime = System.nanoTime();
			elapsedTime = stopTime - startTime;
			System.out.println("Running time algorithm " + algorithm + ": " + (elapsedTime * (Math.pow(10, -6))) + " ms");
		}
		System.out.println("\nAnalysis:\n"
				+ "Algorithm 1 is O(N^3) so it has the longest running time\n"
				+ "Algorithm 2 is O(N^2) so it has shorter running time than algorithm 1"
				+ " but longer running time than algorithm 3 and 4.\n"
				+ "Algorithm 3 is O(NlogN) so it has shorter running time than algorithm 1 and 2"
				+ " but longer running time than algorithm 4.\n"
				+ "Algorithm 4 is O(N) and has the shortest running time.");
	}
	
	/**
	 * Generates an array of certain size, with ints
	 * within range MAX and MIN.
	 */
	public static int[] getArrayOfSize(int size){
		int[] array = new int[size];
		Random rand = new Random();
		for(int i = 0; i < size; i++){
			array[i] = rand.nextInt((MAX - MIN) + 1) + MIN;
		}
		return array;
	}
	
	/**
	 * Cubic maximum contiguous subsequence sum algorithm.
	 */
	public static int maxSubSum1(int[] a) {
		int maxSum = 0;
		for (int i = 0; i < a.length; i++)
			for (int j = i; j < a.length; j++) {
				int thisSum = 0;

				for (int k = i; k <= j; k++)
					thisSum += a[k];

				if (thisSum > maxSum)
					maxSum = thisSum;
			}
		return maxSum;
	}

	/**
	 * Quadratic maximum contiguous subsequence sum algorithm.
	 */
	public static int maxSubSum2(int[] a) {
		int maxSum = 0;

		for (int i = 0; i < a.length; i++) {
			int thisSum = 0;
			for (int j = i; j < a.length; j++) {
				thisSum += a[j];

				if (thisSum > maxSum)
					maxSum = thisSum;
			}
		}

		return maxSum;
	}
	
	/**
	 * Driver for divide-and-conquer maximum contiguous subsequence sum
	 * algorithm.
	 */
	public static int maxSubSum3(int[] a) {
		return maxSumRec(a, 0, a.length - 1);
	}
	
	/**
	 * Recursive maximum contiguous subsequence sum algorithm. Finds maximum sum
	 * in subarray spanning a[left..right]. Does not attempt to maintain actual
	 * best sequence.
	 */
	private static int maxSumRec(int[] a, int left, int right) {
		if (left == right) // Base case
			if (a[left] > 0)
				return a[left];
			else
				return 0;

		int center = (left + right) / 2;
		int maxLeftSum = maxSumRec(a, left, center);
		int maxRightSum = maxSumRec(a, center + 1, right);

		int maxLeftBorderSum = 0, leftBorderSum = 0;
		for (int i = center; i >= left; i--) {
			leftBorderSum += a[i];
			if (leftBorderSum > maxLeftBorderSum)
				maxLeftBorderSum = leftBorderSum;
		}

		int maxRightBorderSum = 0, rightBorderSum = 0;
		for (int i = center + 1; i <= right; i++) {
			rightBorderSum += a[i];
			if (rightBorderSum > maxRightBorderSum)
				maxRightBorderSum = rightBorderSum;
		}
		
		return max3(maxLeftSum, maxRightSum, maxLeftBorderSum + maxRightBorderSum);
	}
	
	/**
	 * Helper function for maxSumRec - returns the largest of 3 numbers
	 */
	private static int max3(int maxLeftSum, int maxRightSum, int i) {
		if(maxLeftSum > maxRightSum && maxLeftSum > i){
			return maxLeftSum;
		} else if (maxRightSum > maxLeftSum && maxRightSum > i){
			return maxRightSum;
		} else {
			return i;
		}
	}
	
	/**
	 * Linear-time maximum contiguous subsequence sum algorithm.
	 */
	public static int maxSubSum4(int[] a) {
		int maxSum = 0, thisSum = 0;

		for (int j = 0; j < a.length; j++) {
			thisSum += a[j];

			if (thisSum > maxSum)
				maxSum = thisSum;
			else if (thisSum < 0)
				thisSum = 0;
		}

		return maxSum;
	}

}
