package question02;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for question02.DuplicateRemover class
 * Tests various scenarios including edge cases and the sample input
 */
public class DuplicateRemoverTest {

    @Test
    @DisplayName("Test sample input from problem")
    public void testSampleInput() {
        int[][] input = {
                {1, 3, 1, 2, 3, 4, 4, 3, 5},
                {1, 1, 1, 1, 1, 1, 1}
        };

        int[][] expected = {
                {1, 3, 0, 2, 0, 4, 0, 0, 5},
                {1, 0, 0, 0, 0, 0, 0}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test empty arrays")
    public void testEmptyArrays() {
        // Test completely empty array
        int[][] emptyArray = {};
        int[][] result1 = DuplicateRemover.removeDuplicates(emptyArray);
        assertArrayEquals(new int[][]{}, result1);

        // Test array with empty rows
        int[][] arrayWithEmptyRows = {
                {},
                {1, 2, 3},
                {}
        };

        int[][] expected = {
                {},
                {1, 2, 3},
                {}
        };

        int[][] result2 = DuplicateRemover.removeDuplicates(arrayWithEmptyRows);
        assertArrayEquals(expected, result2);
    }

    @Test
    @DisplayName("Test null input")
    public void testNullInput() {
        assertNull(DuplicateRemover.removeDuplicates(null));

        // Test array with null rows
        int[][] arrayWithNullRows = {
                {1, 2, 3},
                null,
                {4, 5, 6}
        };

        int[][] result = DuplicateRemover.removeDuplicates(arrayWithNullRows);
        assertEquals(1, result[0][0]);
        assertEquals(2, result[0][1]);
        assertEquals(3, result[0][2]);
        assertNull(result[1]);
        assertEquals(4, result[2][0]);
        assertEquals(5, result[2][1]);
        assertEquals(6, result[2][2]);
    }

    @Test
    @DisplayName("Test array with no duplicates")
    public void testNoDuplicates() {
        int[][] input = {
                {1, 2, 3, 4, 5},
                {10, 20, 30},
                {7}
        };

        int[][] expected = {
                {1, 2, 3, 4, 5},
                {10, 20, 30},
                {7}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test array with all duplicates")
    public void testAllDuplicates() {
        int[][] input = {
                {5, 5, 5, 5},
                {1, 1},
                {9, 9, 9}
        };

        int[][] expected = {
                {5, 0, 0, 0},
                {1, 0},
                {9, 0, 0}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test single element rows")
    public void testSingleElementRows() {
        int[][] input = {
                {1},
                {2},
                {3}
        };

        int[][] expected = {
                {1},
                {2},
                {3}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test variable row lengths")
    public void testVariableRowLengths() {
        int[][] input = {
                {1, 2},
                {3, 4, 5, 6, 3},
                {7, 8, 9, 10, 11, 12, 7, 8},
                {13}
        };

        int[][] expected = {
                {1, 2},
                {3, 4, 5, 6, 0},
                {7, 8, 9, 10, 11, 12, 0, 0},
                {13}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test negative numbers")
    public void testNegativeNumbers() {
        int[][] input = {
                {-1, 2, -1, 3, -2, -2},
                {-5, -5, -5}
        };

        int[][] expected = {
                {-1, 2, 0, 3, -2, 0},
                {-5, 0, 0}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test zeros in input")
    public void testZerosInInput() {
        int[][] input = {
                {0, 1, 0, 2, 0},
                {0, 0, 3}
        };

        int[][] expected = {
                {0, 1, 0, 2, 0}, // First 0 kept, second becomes 0 (stays same), third becomes 0 (stays same)
                {0, 0, 3}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test large numbers within constraints")
    public void testLargeNumbers() {
        int[][] input = {
                {100000, 50000, 100000, 75000},
                {999999, 999999}
        };

        int[][] expected = {
                {100000, 50000, 0, 75000},
                {999999, 0}
        };

        int[][] result = DuplicateRemover.removeDuplicates(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test original array is not modified")
    public void testOriginalArrayNotModified() {
        int[][] original = {
                {1, 2, 1, 3},
                {4, 4, 5}
        };

        // Create a copy to compare with later
        int[][] originalCopy = {
                {1, 2, 1, 3},
                {4, 4, 5}
        };

        DuplicateRemover.removeDuplicates(original);

        // Verify original array is unchanged
        assertArrayEquals(originalCopy, original);
    }

    @Test
    @DisplayName("Test performance with maximum constraints")
    public void testMaxConstraints() {
        // Test with larger array (within reasonable limits for unit test)
        int size = 1000; // Reduced from 100,000 for reasonable test execution time
        int[][] input = new int[100][];

        for (int i = 0; i < 100; i++) {
            input[i] = new int[size];
            for (int j = 0; j < size; j++) {
                input[i][j] = j % 10; // This will create many duplicates
            }
        }

        // This should complete without throwing any exceptions
        long startTime = System.currentTimeMillis();
        int[][] result = DuplicateRemover.removeDuplicates(input);
        long endTime = System.currentTimeMillis();

        assertNotNull(result);
        assertEquals(100, result.length);

        // Verify first row has correct pattern (0,1,2,3,4,5,6,7,8,9,0,0,0,...)
        assertEquals(0, result[0][0]); // First occurrence of 0
        assertEquals(1, result[0][1]); // First occurrence of 1
        assertEquals(0, result[0][10]); // Second occurrence of 0 should be replaced

        System.out.println("Performance test completed in " + (endTime - startTime) + " ms");
    }
}