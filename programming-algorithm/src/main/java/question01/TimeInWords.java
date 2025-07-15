package question01;

import java.util.Scanner;

/**
 * Question 1: Time in Words Converter
 * Converts numerical time format (H:M) to word format
 */
public class TimeInWords {

    private static final String[] NUMBERS = {
            "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen", "twenty"
    };

    private static final String[] TENS = {
            "", "", "twenty", "thirty", "forty", "fifty"
    };

    private static final String[] HOURS = {
            "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve"
    };

    /**
     * Converts time to words format
     * @param hours Hour component (1-12)
     * @param minutes Minute component (0-59)
     * @return Time in words format
     * @throws IllegalArgumentException for invalid input
     */
    public static String convertTimeToWords(int hours, int minutes) {
        // Validate input
        if (hours < 1 || hours > 12) {
            throw new IllegalArgumentException("Hours must be between 1 and 12, got: " + hours);
        }
        if (minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Minutes must be between 0 and 59, got: " + minutes);
        }

        if (minutes == 0) {
            return HOURS[hours] + " o'clock";
        }

        if (minutes == 15) {
            return "quarter past " + HOURS[hours];
        }

        if (minutes == 30) {
            return "half past " + HOURS[hours];
        }

        if (minutes == 45) {
            int nextHour = (hours == 12) ? 1 : hours + 1;
            return "quarter to " + HOURS[nextHour];
        }

        if (minutes <= 30) {
            String minuteWord = convertMinutesToWords(minutes);
            String pluralForm = (minutes == 1) ? " minute past " : " minutes past ";
            return minuteWord + pluralForm + HOURS[hours];
        } else {
            int minutesToNext = 60 - minutes;
            int nextHour = (hours == 12) ? 1 : hours + 1;
            String minuteWord = convertMinutesToWords(minutesToNext);
            String pluralForm = (minutesToNext == 1) ? " minute to " : " minutes to ";
            return minuteWord + pluralForm + HOURS[nextHour];
        }
    }

    /**
     * Converts minutes (1-59) to word format
     * @param minutes The minute value to convert
     * @return Minutes in word format
     */
    private static String convertMinutesToWords(int minutes) {
        if (minutes <= 20) {
            return NUMBERS[minutes];
        }

        int tens = minutes / 10;
        int ones = minutes % 10;

        if (ones == 0) {
            return TENS[tens];
        } else {
            return TENS[tens] + "-" + NUMBERS[ones];
        }
    }

    /**
     * Main method for interactive input
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Enter hours (1-12):");
            int hours = scanner.nextInt();

            System.out.println("Enter minutes (0-59):");
            int minutes = scanner.nextInt();

            String result = convertTimeToWords(hours, minutes);
            System.out.println(result.substring(0, 1).toUpperCase() + result.substring(1));

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Invalid input. Please enter valid integers.");
        } finally {
            scanner.close();
        }
    }
}