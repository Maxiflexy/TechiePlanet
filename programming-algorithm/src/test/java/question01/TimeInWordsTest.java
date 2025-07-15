package question01;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for question01.TimeInWords class
 * Tests edge cases, normal cases, and error conditions
 */
public class TimeInWordsTest {

    @Test
    @DisplayName("Test exact hours (o'clock)")
    public void testOClock() {
        assertEquals("five o'clock", TimeInWords.convertTimeToWords(5, 0));
        assertEquals("twelve o'clock", TimeInWords.convertTimeToWords(12, 0));
        assertEquals("one o'clock", TimeInWords.convertTimeToWords(1, 0));
    }

    @Test
    @DisplayName("Test quarter hours")
    public void testQuarterHours() {
        assertEquals("quarter past five", TimeInWords.convertTimeToWords(5, 15));
        assertEquals("quarter to six", TimeInWords.convertTimeToWords(5, 45));
        assertEquals("quarter past twelve", TimeInWords.convertTimeToWords(12, 15));
        assertEquals("quarter to one", TimeInWords.convertTimeToWords(12, 45));
    }

    @Test
    @DisplayName("Test half past")
    public void testHalfPast() {
        assertEquals("half past five", TimeInWords.convertTimeToWords(5, 30));
        assertEquals("half past one", TimeInWords.convertTimeToWords(1, 30));
        assertEquals("half past twelve", TimeInWords.convertTimeToWords(12, 30));
    }

    @Test
    @DisplayName("Test minutes past (1-30)")
    public void testMinutesPast() {
        assertEquals("one minute past five", TimeInWords.convertTimeToWords(5, 1));
        assertEquals("ten minutes past five", TimeInWords.convertTimeToWords(5, 10));
        assertEquals("twenty-eight minutes past five", TimeInWords.convertTimeToWords(5, 28));
        assertEquals("twenty-nine minutes past eleven", TimeInWords.convertTimeToWords(11, 29));
    }

    @Test
    @DisplayName("Test minutes to (31-59)")
    public void testMinutesTo() {
        assertEquals("twenty-nine minutes to six", TimeInWords.convertTimeToWords(5, 31));
        assertEquals("twenty minutes to six", TimeInWords.convertTimeToWords(5, 40));
        assertEquals("thirteen minutes to six", TimeInWords.convertTimeToWords(5, 47));
        assertEquals("one minute to six", TimeInWords.convertTimeToWords(5, 59));
    }

    @Test
    @DisplayName("Test edge case: minute 59")
    public void testMinute59() {
        assertEquals("one minute to six", TimeInWords.convertTimeToWords(5, 59));
        assertEquals("one minute to one", TimeInWords.convertTimeToWords(12, 59));
        assertEquals("one minute to two", TimeInWords.convertTimeToWords(1, 59));
    }

    @Test
    @DisplayName("Test hour wraparound from 12 to 1")
    public void testHourWraparound() {
        assertEquals("quarter to one", TimeInWords.convertTimeToWords(12, 45));
        assertEquals("twenty minutes to one", TimeInWords.convertTimeToWords(12, 40));
        assertEquals("one minute to one", TimeInWords.convertTimeToWords(12, 59));
    }

    @Test
    @DisplayName("Test sample input from problem")
    public void testSampleInput() {
        assertEquals("thirteen minutes to six", TimeInWords.convertTimeToWords(5, 47));
    }

    @Test
    @DisplayName("Test invalid hours")
    public void testInvalidHours() {
        // Test hours < 1
        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(0, 30);
        });

        // Test hours > 12
        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(13, 30);
        });

        // Test negative hours
        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(-1, 30);
        });
    }

    @Test
    @DisplayName("Test invalid minutes")
    public void testInvalidMinutes() {
        // Test minutes < 0
        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(5, -1);
        });

        // Test minutes > 59
        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(5, 60);
        });

        // Test large invalid minutes
        assertThrows(IllegalArgumentException.class, () -> {
            TimeInWords.convertTimeToWords(5, 100);
        });
    }

    @Test
    @DisplayName("Test boundary values")
    public void testBoundaryValues() {
        // Minimum valid values
        assertEquals("one minute past one", TimeInWords.convertTimeToWords(1, 1));

        // Maximum valid values
        assertEquals("one minute to one", TimeInWords.convertTimeToWords(12, 59));

        // Hour boundaries
        assertEquals("twelve o'clock", TimeInWords.convertTimeToWords(12, 0));
        assertEquals("one o'clock", TimeInWords.convertTimeToWords(1, 0));
    }

    @Test
    @DisplayName("Test all special minute cases")
    public void testSpecialMinuteCases() {
        // Test all quarter-related times
        assertEquals("quarter past three", TimeInWords.convertTimeToWords(3, 15));
        assertEquals("half past three", TimeInWords.convertTimeToWords(3, 30));
        assertEquals("quarter to four", TimeInWords.convertTimeToWords(3, 45));

        // Test exact minute boundaries
        assertEquals("twenty-nine minutes past three", TimeInWords.convertTimeToWords(3, 29));
        assertEquals("twenty-nine minutes to four", TimeInWords.convertTimeToWords(3, 31)); // 60-31=29 minutes to next hour
    }
}