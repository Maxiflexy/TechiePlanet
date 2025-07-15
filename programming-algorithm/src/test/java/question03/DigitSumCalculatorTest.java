package question03;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for DigitSumCalculator class
 * Tests both Part A (sum of digits) and Part B (digital root) functionality
 */
public class DigitSumCalculatorTest {

    // Part A Tests - Sum of Digits

    @Test
    @DisplayName("Test sample input from problem")
    public void testSampleInput() {
        String sampleInput = "1234445123444512344451234445123444512344451234445";
        long result = DigitSumCalculator.sumOfDigits(sampleInput);
        assertEquals(161, result);
    }

    @Test
    @DisplayName("Test single digit")
    public void testSingleDigit() {
        assertEquals(5, DigitSumCalculator.sumOfDigits("5"));
        assertEquals(0, DigitSumCalculator.sumOfDigits("0"));
        assertEquals(9, DigitSumCalculator.sumOfDigits("9"));
    }

    @Test
    @DisplayName("Test simple multi-digit numbers")
    public void testSimpleMultiDigit() {
        assertEquals(6, DigitSumCalculator.sumOfDigits("123")); // 1+2+3
        assertEquals(10, DigitSumCalculator.sumOfDigits("1234")); // 1+2+3+4
        assertEquals(15, DigitSumCalculator.sumOfDigits("12345")); // 1+2+3+4+5
    }

    @Test
    @DisplayName("Test empty string")
    public void testEmptyString() {
        assertEquals(0, DigitSumCalculator.sumOfDigits(""));
    }

    @Test
    @DisplayName("Test string with zeros")
    public void testWithZeros() {
        assertEquals(6, DigitSumCalculator.sumOfDigits("102030")); // 1+0+2+0+3+0
        assertEquals(0, DigitSumCalculator.sumOfDigits("0000"));
        assertEquals(1, DigitSumCalculator.sumOfDigits("0001"));
    }

    @Test
    @DisplayName("Test large numbers")
    public void testLargeNumbers() {
        // Test with repeated 9s
        assertEquals(45, DigitSumCalculator.sumOfDigits("999999999")); // 9*5 = 45

        // Test maximum constraint length (100 characters)
        String hundredNines = "9".repeat(100);
        assertEquals(900, DigitSumCalculator.sumOfDigits(hundredNines)); // 9*100 = 900
    }

    @Test
    @DisplayName("Test invalid inputs for sum of digits")
    public void testInvalidInputsSumOfDigits() {
        // Test null input
        assertThrows(IllegalArgumentException.class, () -> {
            DigitSumCalculator.sumOfDigits(null);
        });

        // Test non-digit characters
        assertThrows(IllegalArgumentException.class, () -> {
            DigitSumCalculator.sumOfDigits("123a456");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DigitSumCalculator.sumOfDigits("12.34");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DigitSumCalculator.sumOfDigits("12-34");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DigitSumCalculator.sumOfDigits(" 123 ");
        });
    }

    // Part B Tests - Digital Root

    @Test
    @DisplayName("Test digital root with example from problem")
    public void testDigitalRootExample() {
        // Example: 1234445 -> 23 -> 5
        int result = DigitSumCalculator.digitalRootRecursive("1234445");
        assertEquals(5, result);

        // Verify with iterative method
        int resultIter = DigitSumCalculator.digitalRootIterative("1234445");
        assertEquals(5, resultIter);

        // Verify with mathematical method
        int resultMath = DigitSumCalculator.digitalRootMathematical("1234445");
        assertEquals(5, resultMath);
    }

    @Test
    @DisplayName("Test digital root for single digits")
    public void testDigitalRootSingleDigits() {
        for (int i = 0; i <= 9; i++) {
            String input = String.valueOf(i);
            assertEquals(i, DigitSumCalculator.digitalRootRecursive(input));
            assertEquals(i, DigitSumCalculator.digitalRootIterative(input));
            assertEquals(i, DigitSumCalculator.digitalRootMathematical(input));
        }
    }

    @Test
    @DisplayName("Test digital root for known cases")
    public void testDigitalRootKnownCases() {
        // Test cases where we know the digital root
        assertEquals(1, DigitSumCalculator.digitalRootRecursive("19")); // 1+9=10, 1+0=1
        assertEquals(2, DigitSumCalculator.digitalRootRecursive("38")); // 3+8=11, 1+1=2
        assertEquals(3, DigitSumCalculator.digitalRootRecursive("999")); // 9+9+9=27, 2+7=9... wait, let me recalculate

        // Let me verify: 999 -> 9+9+9 = 27 -> 2+7 = 9
        assertEquals(9, DigitSumCalculator.digitalRootRecursive("999"));

        // Test 123: 1+2+3 = 6
        assertEquals(6, DigitSumCalculator.digitalRootRecursive("123"));

        // Test 789: 7+8+9 = 24 -> 2+4 = 6
        assertEquals(6, DigitSumCalculator.digitalRootRecursive("789"));

        // Test 99: 9+9 = 18 -> 1+8 = 9
        assertEquals(9, DigitSumCalculator.digitalRootRecursive("99"));
    }

    @Test
    @DisplayName("Test digital root methods consistency")
    public void testDigitalRootMethodsConsistency() {
        String[] testCases = {
                "123", "456", "789", "1234", "5678", "9999", "12345",
                "987654321", "1111111111", "9876543210"
        };

        for (String testCase : testCases) {
            int recursive = DigitSumCalculator.digitalRootRecursive(testCase);
            int iterative = DigitSumCalculator.digitalRootIterative(testCase);
            int mathematical = DigitSumCalculator.digitalRootMathematical(testCase);

            assertEquals(recursive, iterative,
                    "Recursive and iterative methods disagree for input: " + testCase);
            assertEquals(recursive, mathematical,
                    "Recursive and mathematical methods disagree for input: " + testCase);
        }
    }

    @Test
    @DisplayName("Test digital root with sample input")
    public void testDigitalRootSampleInput() {
        String sampleInput = "1234445123444512344451234445123444512344451234445";

        // First verify sum is 161
        long sum = DigitSumCalculator.sumOfDigits(sampleInput);
        assertEquals(161, sum);

        // Digital root of 161: 1+6+1 = 8
        int digitalRoot = DigitSumCalculator.digitalRootRecursive(sampleInput);
        assertEquals(8, digitalRoot);

        // Verify consistency across methods
        assertEquals(8, DigitSumCalculator.digitalRootIterative(sampleInput));
        assertEquals(8, DigitSumCalculator.digitalRootMathematical(sampleInput));
    }

    @Test
    @DisplayName("Test digital root with zeros")
    public void testDigitalRootWithZeros() {
        assertEquals(0, DigitSumCalculator.digitalRootRecursive("0"));
        assertEquals(0, DigitSumCalculator.digitalRootRecursive("000"));
        assertEquals(1, DigitSumCalculator.digitalRootRecursive("0001"));
        assertEquals(3, DigitSumCalculator.digitalRootRecursive("003"));
    }

    @Test
    @DisplayName("Test digital root boundary cases")
    public void testDigitalRootBoundaries() {
        // Test numbers that sum to exactly 9, 18, 27, etc.
        assertEquals(9, DigitSumCalculator.digitalRootRecursive("9"));
        assertEquals(9, DigitSumCalculator.digitalRootRecursive("18"));    // 1+8=9
        assertEquals(9, DigitSumCalculator.digitalRootRecursive("27"));    // 2+7=9
        assertEquals(9, DigitSumCalculator.digitalRootRecursive("36"));    // 3+6=9
        assertEquals(9, DigitSumCalculator.digitalRootRecursive("45"));    // 4+5=9

        // Test numbers that sum to 10, 19, 28, etc. (should give digital root 1)
        assertEquals(1, DigitSumCalculator.digitalRootRecursive("19"));    // 1+9=10, 1+0=1
        assertEquals(1, DigitSumCalculator.digitalRootRecursive("28"));    // 2+8=10, 1+0=1
        assertEquals(1, DigitSumCalculator.digitalRootRecursive("37"));    // 3+7=10, 1+0=1
    }

    @Test
    @DisplayName("Test performance with maximum constraint")
    public void testPerformanceMaxConstraint() {
        // Test with 100-character string (maximum constraint)
        String maxInput = "1".repeat(100); // Sum should be 100, digital root should be 1

        long startTime = System.currentTimeMillis();

        long sum = DigitSumCalculator.sumOfDigits(maxInput);
        assertEquals(100, sum);

        int digitalRoot = DigitSumCalculator.digitalRootRecursive(maxInput);
        assertEquals(1, digitalRoot); // 100 -> 1+0+0 = 1

        long endTime = System.currentTimeMillis();

        // Should complete quickly (within reasonable time)
        assertTrue((endTime - startTime) < 1000, "Performance test should complete within 1 second");

        System.out.println("Performance test (100 characters) completed in " + (endTime - startTime) + " ms");
    }

    @Test
    @DisplayName("Test edge case: very long string with all 9s")
    public void testLongStringAllNines() {
        // Test with 50 nines - digital root should always be 9
        String fiftyNines = "9".repeat(50);

        long sum = DigitSumCalculator.sumOfDigits(fiftyNines);
        assertEquals(450, sum); // 50 * 9 = 450

        int digitalRoot = DigitSumCalculator.digitalRootRecursive(fiftyNines);
        assertEquals(9, digitalRoot); // Any multiple of 9 has digital root 9

        // Verify with other methods
        assertEquals(9, DigitSumCalculator.digitalRootIterative(fiftyNines));
        assertEquals(9, DigitSumCalculator.digitalRootMathematical(fiftyNines));
    }
}
