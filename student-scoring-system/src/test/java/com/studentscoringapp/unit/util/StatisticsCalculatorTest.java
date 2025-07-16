package com.studentscoringapp.unit.util;

import com.studentscoringapp.util.StatisticsCalculator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsCalculatorTest {

    @Test
    void calculateMean_WithValidScores() {
        // Given
        List<Double> scores = Arrays.asList(80.0, 85.0, 90.0, 75.0, 95.0);

        // When
        Double mean = StatisticsCalculator.calculateMean(scores);

        // Then
        assertEquals(85.0, mean);
    }

    @Test
    void calculateMedian_WithOddNumberOfScores() {
        // Given
        List<Double> scores = Arrays.asList(80.0, 85.0, 90.0, 75.0, 95.0);

        // When
        Double median = StatisticsCalculator.calculateMedian(scores);

        // Then
        assertEquals(85.0, median);
    }

    @Test
    void calculateMedian_WithEvenNumberOfScores() {
        // Given
        List<Double> scores = Arrays.asList(80.0, 85.0, 90.0, 95.0);

        // When
        Double median = StatisticsCalculator.calculateMedian(scores);

        // Then
        assertEquals(87.5, median);
    }

    @Test
    void calculateMode_WithRepeatingValues() {
        // Given
        List<Double> scores = Arrays.asList(80.0, 85.0, 80.0, 90.0, 85.0);

        // When
        List<Double> modes = StatisticsCalculator.calculateMode(scores);

        // Then
        assertEquals(2, modes.size());
        assertTrue(modes.contains(80.0));
        assertTrue(modes.contains(85.0));
    }

    @Test
    void calculateMode_WithNoRepeatingValues() {
        // Given
        List<Double> scores = Arrays.asList(80.0, 85.0, 90.0, 95.0);

        // When
        List<Double> modes = StatisticsCalculator.calculateMode(scores);

        // Then
        assertTrue(modes.isEmpty());
    }
}