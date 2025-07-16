package com.studentscoringapp.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Statistics Utility Tests")
class StatisticsUtilTest {

    @Test
    @DisplayName("Calculate mean for normal scores")
    void shouldCalculateMeanForNormalScores() {
        // Given
        List<Integer> scores = Arrays.asList(85, 90, 78, 92, 88);

        // When
        Double result = StatisticsUtil.calculateMean(scores);

        // Then
        assertEquals(86.6, result, 0.1);
    }

    @Test
    @DisplayName("Calculate mean for empty list")
    void shouldReturnZeroForEmptyList() {
        // Given
        List<Integer> scores = Collections.emptyList();

        // When
        Double result = StatisticsUtil.calculateMean(scores);

        // Then
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Calculate median for odd number of scores")
    void shouldCalculateMedianForOddScores() {
        // Given
        List<Integer> scores = Arrays.asList(85, 90, 78, 92, 88);

        // When
        Double result = StatisticsUtil.calculateMedian(scores);

        // Then
        assertEquals(88.0, result);
    }

    @Test
    @DisplayName("Calculate median for even number of scores")
    void shouldCalculateMedianForEvenScores() {
        // Given
        List<Integer> scores = Arrays.asList(85, 90, 78, 92);

        // When
        Double result = StatisticsUtil.calculateMedian(scores);

        // Then
        assertEquals(87.5, result);
    }

    @Test
    @DisplayName("Calculate mode for scores with clear mode")
    void shouldCalculateModeForScores() {
        // Given
        List<Integer> scores = Arrays.asList(85, 90, 85, 92, 85, 88);

        // When
        Integer result = StatisticsUtil.calculateMode(scores);

        // Then
        assertEquals(85, result);
    }

    @Test
    @DisplayName("Calculate total for normal scores")
    void shouldCalculateTotalForScores() {
        // Given
        List<Integer> scores = Arrays.asList(85, 90, 78, 92, 88);

        // When
        Integer result = StatisticsUtil.calculateTotal(scores);

        // Then
        assertEquals(433, result);
    }

    @Test
    @DisplayName("Find maximum score")
    void shouldFindMaximumScore() {
        // Given
        List<Integer> scores = Arrays.asList(85, 90, 78, 92, 88);

        // When
        Integer result = StatisticsUtil.findMax(scores);

        // Then
        assertEquals(92, result);
    }

    @Test
    @DisplayName("Find minimum score")
    void shouldFindMinimumScore() {
        // Given
        List<Integer> scores = Arrays.asList(85, 90, 78, 92, 88);

        // When
        Integer result = StatisticsUtil.findMin(scores);

        // Then
        assertEquals(78, result);
    }
}