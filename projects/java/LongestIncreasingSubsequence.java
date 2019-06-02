
public class LongestIncreasingSubsequence {

	public static void main(String[] args) {
		int[] array = new int[10];
		
		for(int i=0; i<array.length; i++) {
			array[i] = (int)(Math.random() * 100);
		}
		
		for (int i=0; i<10; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println();
		
		
		
		int[] maxLength = dpSolution(array);
		
		for (int i=0; i<10; i++) {
			System.out.print(maxLength[i] + " ");
		}
		System.out.println();
		
		
		int max = 0;
		System.out.println("Longest Increasing Subsequence:");
		System.out.println("DP Solution:");
		for (int i=0; i<maxLength.length; i++) {
			if (maxLength[i] > max) {
				max = maxLength[i];
				System.out.print(array[i] + " ");
			}
		}
		System.out.println("\nLength: " + max);
		
		System.out.println("\nRecursive Solution:");
		int answer = getMaxLength(array, array.length-1);
		System.out.println("Length: " + answer);
		
	}
	
	static int[] dpSolution(int[] array) {
		int length = array.length;
		if (length == 0)
			return null;
		else if (length == 1)
			return array;
		else {
			int[] maxLength = new int[length];
			maxLength[0] = 1;
			for (int i=1; i<length; i++) {
				int max = 0;
				for (int j=0; j<i; j++) {
					if ((array[j] < array[i]) && (maxLength[j] > max))
							max = maxLength[j];
				}
				maxLength[i] = 1 + max;
			}
			return maxLength;
		}
	}
	
	static int getMaxLength(int[] array, int index) {
		int max = 0;
		for (int i=index-1; i>=0; i--) {
			int answer = getMaxLength(array, i);
			if (array[i] < array[index] && answer > max) {
					max = answer;
			}
		}
		return 1 + max;
	}

}
