package com.studentscoringapp.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class StatisticsCalculator {

    public static Double calculateMean(List<Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }

        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static Double calculateMedian(List<Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }

        List<Double> sortedScores = scores.stream()
                .sorted()
                .collect(Collectors.toList());

        int size = sortedScores.size();
        if (size % 2 == 0) {
            return (sortedScores.get(size / 2 - 1) + sortedScores.get(size / 2)) / 2.0;
        } else {
            return sortedScores.get(size / 2);
        }
    }

    public static List<Double> calculateMode(List<Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Double, Long> frequencyMap = scores.stream()
                .collect(Collectors.groupingBy(
                        Double::doubleValue,
                        Collectors.counting()
                ));

        long maxFrequency = frequencyMap.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        // If all scores appear only once, there's no mode
        if (maxFrequency == 1) {
            return new ArrayList<>();
        }

        return frequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    public static Double findHighest(List<Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }

        return scores.stream()
                .max(Double::compareTo)
                .orElse(0.0);
    }

    public static Double findLowest(List<Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }

        return scores.stream()
                .min(Double::compareTo)
                .orElse(0.0);
    }
}