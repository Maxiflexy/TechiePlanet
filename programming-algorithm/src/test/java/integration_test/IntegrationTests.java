package integration_test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import question01.TimeInWords;
import question02.DuplicateRemover;
import question03.DigitSumCalculator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests for all classes
 * Tests the interaction between different components and overall system behavior
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTests {

    @Test
    @Order(1)
    @DisplayName("Integration Test 1: All sample inputs work correctly")
    public void testAllSampleInputs() {
        System.out.println("=== Integration Test 1: Sample Inputs ===");

        // Question 1 Sample Test
        String timeResult = TimeInWords.convertTimeToWords(5, 47);
        assertEquals("thirteen minutes to six", timeResult);
        System.out.println("✓ Question 1 Sample: " + timeResult);

        // Question 2 Sample Test
        int[][] arrayInput = {
                {1, 3, 1, 2, 3, 4, 4, 3, 5},
                {1, 1, 1, 1, 1, 1, 1}
        };

        int[][] arrayExpected = {
                {1, 3, 0, 2, 0, 4, 0, 0, 5},
                {1, 0, 0, 0, 0, 0, 0}
        };

        int[][] arrayResult = DuplicateRemover.removeDuplicates(arrayInput);
        assertArrayEquals(arrayExpected, arrayResult);
        System.out.println("✓ Question 2 Sample: Arrays match expected output");

        // Question 3 Sample Test
        String digitString = "1234445123444512344451234445123444512344451234445";
        long digitSum = DigitSumCalculator.sumOfDigits(digitString);
        assertEquals(161, digitSum);

        int digitalRoot = DigitSumCalculator.digitalRootRecursive(digitString);
        assertEquals(8, digitalRoot); // 161 -> 1+6+1 = 8
        System.out.println("✓ Question 3 Sample: Sum=" + digitSum + ", Digital Root=" + digitalRoot);
    }

    @Test
    @Order(2)
    @DisplayName("Integration Test 2: Cross-component data flow")
    public void testCrossComponentDataFlow() {
        System.out.println("\n=== Integration Test 2: Cross-Component Data Flow ===");

        // Scenario: Use time components to create array indices, then sum digits
        int hour = 12;
        int minute = 30;

        // Step 1: Convert time to words and extract length information
        String timeWords = TimeInWords.convertTimeToWords(hour, minute);
        assertEquals("half past twelve", timeWords);

        // Step 2: Use time components to create a test array
        int[][] testArray = {
                {hour, minute, hour, minute + 1}, // Will have duplicates of hour and minute
                {minute, minute, hour}
        };

        int[][] cleanedArray = DuplicateRemover.removeDuplicates(testArray);

        // Expected: {12, 30, 0, 31}, {30, 0, 12}
        assertEquals(12, cleanedArray[0][0]); // First occurrence of 12
        assertEquals(30, cleanedArray[0][1]); // First occurrence of 30
        assertEquals(0, cleanedArray[0][2]);  // Duplicate of 12
        assertEquals(31, cleanedArray[0][3]); // 31 (minute + 1)
        assertEquals(30, cleanedArray[1][0]); // 30 in second row
        assertEquals(0, cleanedArray[1][1]);  // Duplicate of 30
        assertEquals(12, cleanedArray[1][2]); // 12 in second row (different row, so allowed)

        // Step 3: Convert remaining non-zero numbers to string and sum digits
        StringBuilder combinedDigits = new StringBuilder();
        for (int[] row : cleanedArray) {
            for (int value : row) {
                if (value != 0) {
                    combinedDigits.append(value);
                }
            }
        }

        String digitString = combinedDigits.toString(); // Should be "1230313012"
        long digitSum = DigitSumCalculator.sumOfDigits(digitString);
        assertEquals(16, digitSum); // 1+2+3+0+3+1+3+0+1+2 = 16

        int digitalRoot = DigitSumCalculator.digitalRootRecursive(digitString);
        assertEquals(7, digitalRoot); // 16 -> 1+6 = 7

        System.out.println("✓ Time: " + timeWords);
        System.out.println("✓ Combined digits: " + digitString);
        System.out.println("✓ Sum: " + digitSum + ", Digital Root: " + digitalRoot);
    }

    @Test
    @Order(3)
    @DisplayName("Integration Test 3: Error handling across components")
    public void testErrorHandlingAcrossComponents() {
        System.out.println("\n=== Integration Test 3: Error Handling ===");

        // Test that all components handle edge cases gracefully

        // Question 1: Invalid time inputs
        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(0, 30);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(5, 60);
        });

        // Question 2: Null and empty arrays
        assertNull(DuplicateRemover.removeDuplicates(null));

        int[][] emptyArray = {};
        int[][] emptyResult = DuplicateRemover.removeDuplicates(emptyArray);
        assertEquals(0, emptyResult.length);

        // Question 3: Invalid digit strings
        assertThrows(IllegalArgumentException.class, () -> {
            DigitSumCalculator.sumOfDigits(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DigitSumCalculator.sumOfDigits("12a34");
        });

        // Valid edge case: empty string
        assertEquals(0, DigitSumCalculator.sumOfDigits(""));

        System.out.println("✓ All error cases handled appropriately");
    }

    @Test
    @Order(4)
    @DisplayName("Integration Test 4: Performance and scalability")
    public void testPerformanceAndScalability() {
        System.out.println("\n=== Integration Test 4: Performance ===");

        long totalStartTime = System.currentTimeMillis();

        // Test Question 1 with all valid time combinations (performance baseline)
        long timeTestStart = System.currentTimeMillis();
        int timeTestCount = 0;
        for (int h = 1; h <= 12; h++) {
            for (int m = 0; m <= 59; m++) {
                String result = TimeInWords.convertTimeToWords(h, m);
                assertNotNull(result);
                assertFalse(result.isEmpty());
                timeTestCount++;
            }
        }
        long timeTestEnd = System.currentTimeMillis();
        System.out.println("✓ Question 1: " + timeTestCount + " time conversions in " +
                (timeTestEnd - timeTestStart) + " ms");

        // Test Question 2 with moderately large arrays
        long arrayTestStart = System.currentTimeMillis();
        int[][] largeArray = new int[100][];
        for (int i = 0; i < 100; i++) {
            largeArray[i] = new int[100];
            for (int j = 0; j < 100; j++) {
                largeArray[i][j] = j % 10; // Creates patterns with duplicates
            }
        }

        int[][] largeResult = DuplicateRemover.removeDuplicates(largeArray);
        assertNotNull(largeResult);
        assertEquals(100, largeResult.length);
        long arrayTestEnd = System.currentTimeMillis();
        System.out.println("✓ Question 2: 100x100 array processed in " +
                (arrayTestEnd - arrayTestStart) + " ms");

        // Test Question 3 with long digit strings
        long digitTestStart = System.currentTimeMillis();
        String longDigitString = "123456789".repeat(10); // 90 characters
        long sum = DigitSumCalculator.sumOfDigits(longDigitString);
        int digitalRoot = DigitSumCalculator.digitalRootRecursive(longDigitString);
        long digitTestEnd = System.currentTimeMillis();

        assertEquals(450, sum); // (1+2+3+4+5+6+7+8+9) * 10 = 45 * 10 = 450
        assertEquals(9, digitalRoot); // 450 -> 4+5+0 = 9

        System.out.println("✓ Question 3: 90-character string processed in " +
                (digitTestEnd - digitTestStart) + " ms");

        long totalEndTime = System.currentTimeMillis();
        System.out.println("✓ Total integration test time: " + (totalEndTime - totalStartTime) + " ms");

        // Performance assertion: should complete within reasonable time
        assertTrue((totalEndTime - totalStartTime) < 5000,
                "Integration tests should complete within 5 seconds");
    }

    @Test
    @Order(5)
    @DisplayName("Integration Test 5: Real-world scenario simulation")
    public void testRealWorldScenario() {
        System.out.println("\n=== Integration Test 5: Real-World Scenario ===");

        // Scenario: Process daily schedule data
        // Times represent meeting hours, arrays represent attendee IDs with duplicates to remove,
        // and final digit sums represent some kind of validation hash

        int[][] dailySchedule = {
                // 9:00 AM meeting
                {901, 902, 903, 901, 904, 902}, // Remove duplicate attendees

                // 2:30 PM meeting
                {1430, 1431, 1430, 1432, 1433},

                // 5:45 PM meeting
                {1745, 1746, 1747, 1745, 1748}
        };

        System.out.println("Processing daily schedule...");

        // Clean up duplicate attendees
        int[][] cleanSchedule = DuplicateRemover.removeDuplicates(dailySchedule);

        // Verify first meeting: {901, 902, 903, 0, 904, 0}
        assertEquals(901, cleanSchedule[0][0]);
        assertEquals(902, cleanSchedule[0][1]);
        assertEquals(903, cleanSchedule[0][2]);
        assertEquals(0, cleanSchedule[0][3]); // duplicate 901
        assertEquals(904, cleanSchedule[0][4]);
        assertEquals(0, cleanSchedule[0][5]); // duplicate 902

        // Convert meeting times to words for display
        String meeting1 = TimeInWords.convertTimeToWords(9, 0);
        String meeting2 = TimeInWords.convertTimeToWords(2, 30);
        String meeting3 = TimeInWords.convertTimeToWords(5, 45);

        assertEquals("nine o'clock", meeting1);
        assertEquals("half past two", meeting2);
        assertEquals("quarter to six", meeting3);

        // Calculate validation hash for each meeting
        long[] validationHashes = new long[cleanSchedule.length];
        for (int i = 0; i < cleanSchedule.length; i++) {
            StringBuilder attendeeIds = new StringBuilder();
            for (int attendeeId : cleanSchedule[i]) {
                if (attendeeId != 0) {
                    attendeeIds.append(attendeeId);
                }
            }
            validationHashes[i] = DigitSumCalculator.sumOfDigits(attendeeIds.toString());
        }

        // Expected validation hashes:
        // Meeting 1: 901902903904 -> 9+0+1+9+0+2+9+0+3+9+0+4 = 46
        // Meeting 2: 143014311432433 -> 1+4+3+0+1+4+3+1+1+4+3+2+4+3+3 = 38
        // Meeting 3: 174517461747748 -> calculation needed

        assertEquals(46, validationHashes[0]);
        assertEquals(38, validationHashes[1]); // CORRECTED: was 34, now 38

        // Calculate digital roots for final validation
        int digitalRoot1 = DigitSumCalculator.digitalRootIterative("901902903904");
        int digitalRoot2 = DigitSumCalculator.digitalRootIterative("143014311432433");

        assertEquals(1, digitalRoot1); // 46 -> 4+6 = 10 -> 1+0 = 1
        assertEquals(1, digitalRoot2); // CORRECTED: 37 -> 3+7 = 10 -> 1+0 = 1

        System.out.println("✓ Meeting 1 (" + meeting1 + "): Hash=" + validationHashes[0] +
                ", Digital Root=" + digitalRoot1);
        System.out.println("✓ Meeting 2 (" + meeting2 + "): Hash=" + validationHashes[1] +
                ", Digital Root=" + digitalRoot2);
        System.out.println("✓ Meeting 3 (" + meeting3 + "): Hash=" + validationHashes[2]);

        System.out.println("✓ Real-world scenario processed successfully");
    }

    @Test
    @Order(6)
    @DisplayName("Integration Test 6: System boundary testing")
    public void testSystemBoundaries() {
        System.out.println("\n=== Integration Test 6: System Boundaries ===");

        // Test maximum valid inputs for each component

        // Question 1: Maximum valid time
        String maxTime = TimeInWords.convertTimeToWords(12, 59);
        assertEquals("one minute to one", maxTime);

        // Question 1: Minimum valid time
        String minTime = TimeInWords.convertTimeToWords(1, 0);
        assertEquals("one o'clock", minTime);

        // Question 2: Array with maximum reasonable size for testing
        int[][] maxArray = new int[1000][];
        for (int i = 0; i < 1000; i++) {
            maxArray[i] = new int[50];
            for (int j = 0; j < 50; j++) {
                maxArray[i][j] = j % 5; // Creates many duplicates
            }
        }

        long arrayStartTime = System.currentTimeMillis();
        int[][] maxResult = DuplicateRemover.removeDuplicates(maxArray);
        long arrayEndTime = System.currentTimeMillis();

        assertNotNull(maxResult);
        assertEquals(1000, maxResult.length);
        System.out.println("✓ Large array (1000x50) processed in " +
                (arrayEndTime - arrayStartTime) + " ms");

        // Question 3: Maximum constraint string (100 characters)
        String maxDigitString = "9".repeat(100);
        long maxSum = DigitSumCalculator.sumOfDigits(maxDigitString);
        assertEquals(900, maxSum);

        int maxDigitalRoot = DigitSumCalculator.digitalRootMathematical(maxDigitString);
        assertEquals(9, maxDigitalRoot);

        // Test consistency across all digital root methods for boundary case
        assertEquals(maxDigitalRoot, DigitSumCalculator.digitalRootRecursive(maxDigitString));
        assertEquals(maxDigitalRoot, DigitSumCalculator.digitalRootIterative(maxDigitString));

        System.out.println("✓ Maximum constraints handled successfully");

        // Test minimum valid inputs
        assertEquals(0, DigitSumCalculator.sumOfDigits("0"));
        assertEquals(0, DigitSumCalculator.digitalRootRecursive("0"));

        int[][] minArray = {{}};
        int[][] minResult = DuplicateRemover.removeDuplicates(minArray);
        assertEquals(1, minResult.length);
        assertEquals(0, minResult[0].length);

        System.out.println("✓ Minimum valid inputs handled successfully");
    }
}