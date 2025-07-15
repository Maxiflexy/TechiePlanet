package question02;

/**
 * Question 2: Remove Duplicates from Multidimensional Array
 */
public class DuplicateRemover {

    /**
     * Removes duplicates from each row of a multidimensional array
     * Replaces duplicate occurrences with 0, keeping only the first occurrence
     *
     * @param array The input multidimensional array
     * @return New array with duplicates replaced by 0
     */
    public static int[][] removeDuplicates(int[][] array) {
        if (array == null) {
            return null;
        }

        // Create result array with same dimensions
        int[][] result = new int[array.length][];

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                result[i] = null;
                continue;
            }

            result[i] = removeDuplicatesFromRow(array[i]);
        }

        return result;
    }

    /**
     * Removes duplicates from a single row
     * Uses custom logic without built-in contains/containsKey functions
     *
     * @param row The input row array
     * @return New row with duplicates replaced by 0
     */
    private static int[] removeDuplicatesFromRow(int[] row) {
        if (row.length == 0) {
            return new int[0];
        }

        int[] result = new int[row.length];

        // Copy the row to result array first
        for (int i = 0; i < row.length; i++) {
            result[i] = row[i];
        }

        // Process each element in the row
        for (int i = 0; i < result.length; i++) {
            // Check if this element appeared earlier in the array
            if (hasAppearedBefore(result, i, result[i])) {
                result[i] = 0; // Replace duplicate with 0
            }
        }

        return result;
    }

    /**
     * Custom function to check if a value has appeared before the given index
     * This replaces the need for contains() or similar built-in functions
     *
     * @param array The array to search in
     * @param currentIndex The current position
     * @param value The value to search for
     * @return true if value was found before currentIndex, false otherwise
     */
    private static boolean hasAppearedBefore(int[] array, int currentIndex, int value) {
        for (int i = 0; i < currentIndex; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Utility method to print multidimensional array in readable format
     *
     * @param array The array to print
     */
    public static void printArray(int[][] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) System.out.print(",\n ");

            if (array[i] == null) {
                System.out.print("null");
                continue;
            }

            System.out.print("{");
            for (int j = 0; j < array[i].length; j++) {
                if (j > 0) System.out.print(", ");
                System.out.print(array[i][j]);
            }
            System.out.print("}");
        }
        System.out.println("]");
    }

    /**
     * Main method demonstrating the functionality with sample input
     */
    public static void main(String[] args) {
        // Sample input from problem
        int[][] sampleInput = {
                {1, 3, 1, 2, 3, 4, 4, 3, 5},
                {1, 1, 1, 1, 1, 1, 1}
        };

        System.out.println("Sample Input:");
        printArray(sampleInput);

        int[][] result = removeDuplicates(sampleInput);

        System.out.println("\nSample Output:");
        printArray(result);

        // Additional test cases
        System.out.println("\n--- Additional Test Cases ---");

        // Test case with different row lengths
        int[][] testCase2 = {
                {5, 2, 5, 1},
                {10, 20, 30},
                {7, 7, 7, 7, 7}
        };

        System.out.println("\nTest Case 2 Input:");
        printArray(testCase2);

        int[][] result2 = removeDuplicates(testCase2);

        System.out.println("Test Case 2 Output:");
        printArray(result2);
    }
}