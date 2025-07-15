package question03;

import java.util.Scanner;

/**
 * Question 3: Sum of Digits Calculator
 *
 * Part A: Recursive function to find sum of digits in a string
 * Part B: Digital root calculation (keep summing until single digit)
 */
public class DigitSumCalculator {

    /**
     * Part A: Recursive function to calculate sum of all digits in a string
     *
     * @param digitString String containing digits
     * @return Sum of all digits in the string
     * @throws IllegalArgumentException if string contains non-digit characters
     */
    public static long sumOfDigits(String digitString) {
        // Input validation
        if (digitString == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }

        if (digitString.isEmpty()) {
            return 0;
        }

        // Validate that string contains only digits
        for (char c : digitString.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("String must contain only digits, found: " + c);
            }
        }

        return sumOfDigitsRecursive(digitString, 0);
    }

    /**
     * Recursive helper method for summing digits
     *
     * @param digitString The string containing digits
     * @param index Current position in the string
     * @return Sum of digits from current position to end
     */
    private static long sumOfDigitsRecursive(String digitString, int index) {
        // Base case: reached end of string
        if (index >= digitString.length()) {
            return 0;
        }

        // Get current digit and convert to integer
        char currentChar = digitString.charAt(index);
        int currentDigit = Character.getNumericValue(currentChar);

        // Recursive case: current digit + sum of remaining digits
        return currentDigit + sumOfDigitsRecursive(digitString, index + 1);
    }

    /**
     * Part B: Calculate digital root using recursive approach
     * Digital root is obtained by repeatedly summing digits until single digit remains
     *
     * @param digitString String containing digits
     * @return Digital root (single digit 1-9, or 0 if input is 0)
     */
    public static int digitalRootRecursive(String digitString) {
        long sum = sumOfDigits(digitString);
        return digitalRootRecursiveHelper(sum);
    }

    /**
     * Recursive helper for digital root calculation
     *
     * @param number The number to find digital root for
     * @return Digital root of the number
     */
    private static int digitalRootRecursiveHelper(long number) {
        // Base case: single digit
        if (number < 10) {
            return (int) number;
        }

        // Recursive case: sum digits and call again
        long digitSum = sumDigitsOfNumber(number);
        return digitalRootRecursiveHelper(digitSum);
    }

    /**
     * Part B Alternative: Calculate digital root using iterative approach
     *
     * @param digitString String containing digits
     * @return Digital root (single digit 1-9, or 0 if input is 0)
     */
    public static int digitalRootIterative(String digitString) {
        long sum = sumOfDigits(digitString);

        while (sum >= 10) {
            sum = sumDigitsOfNumber(sum);
        }

        return (int) sum;
    }

    /**
     * Mathematical formula for digital root (optimized approach)
     * Digital root can be calculated directly using modular arithmetic
     *
     * @param digitString String containing digits
     * @return Digital root using mathematical formula
     */
    public static int digitalRootMathematical(String digitString) {
        long sum = sumOfDigits(digitString);

        if (sum == 0) {
            return 0;
        }

        // Mathematical formula: digital_root = 1 + ((n - 1) % 9)
        return 1 + (int)((sum - 1) % 9);
    }

    /**
     * Helper method to sum digits of a number
     *
     * @param number The number whose digits to sum
     * @return Sum of digits
     */
    private static long sumDigitsOfNumber(long number) {
        long sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    /**
     * Utility method to demonstrate the step-by-step digital root calculation
     *
     * @param digitString Input string
     */
    public static void demonstrateDigitalRoot(String digitString) {
        System.out.println("Input: " + digitString);

        long sum = sumOfDigits(digitString);
        System.out.println("First sum: " + formatDigitSum(digitString) + " = " + sum);

        while (sum >= 10) {
            String sumStr = String.valueOf(sum);
            long nextSum = sumDigitsOfNumber(sum);
            System.out.println("Next sum: " + formatDigitSum(sumStr) + " = " + nextSum);
            sum = nextSum;
        }

        System.out.println("Digital root = " + sum);
    }

    /**
     * Formats digit sum for display (e.g., "1234" -> "1+2+3+4")
     *
     * @param digitString String of digits
     * @return Formatted string with plus signs
     */
    private static String formatDigitSum(String digitString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digitString.length(); i++) {
            if (i > 0) sb.append("+");
            sb.append(digitString.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Main method for testing and demonstration
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Sample input from problem
            String sampleInput = "1234445123444512344451234445123444512344451234445";
            System.out.println("=== Sample Input Test ===");
            System.out.println("Input: " + sampleInput);

            long sumResult = sumOfDigits(sampleInput);
            System.out.println("Sum of digits: " + sumResult);

            int digitalRoot = digitalRootRecursive(sampleInput);
            System.out.println("Digital root (recursive): " + digitalRoot);

            int digitalRootIter = digitalRootIterative(sampleInput);
            System.out.println("Digital root (iterative): " + digitalRootIter);

            int digitalRootMath = digitalRootMathematical(sampleInput);
            System.out.println("Digital root (mathematical): " + digitalRootMath);

            // Demonstrate with example from problem description
            System.out.println("\n=== Example Demonstration ===");
            demonstrateDigitalRoot("1234445");

            // Interactive input
            System.out.println("\n=== Interactive Test ===");
            System.out.println("Enter a string of digits (or 'quit' to exit):");

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();

                if ("quit".equalsIgnoreCase(input)) {
                    break;
                }

                try {
                    long sum = sumOfDigits(input);
                    int digitalRoot1 = digitalRootRecursive(input);

                    System.out.println("Sum of digits: " + sum);
                    System.out.println("Digital root: " + digitalRoot1);

                    if (input.length() <= 20) { // Only show step-by-step for shorter inputs
                        System.out.println("Step-by-step:");
                        demonstrateDigitalRoot(input);
                    }

                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + e.getMessage());
                }

                System.out.println("\nEnter another string of digits (or 'quit' to exit):");
            }

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}