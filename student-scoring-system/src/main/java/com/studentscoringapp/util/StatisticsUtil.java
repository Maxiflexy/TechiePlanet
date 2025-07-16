package com.studentscoringapp.util;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsUtil {

    public static Double calculateMean(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }
        return scores.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static Double calculateMedian(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }

        List<Integer> sortedScores = scores.stream()
                .sorted()
                .collect(Collectors.toList());

        int size = sortedScores.size();
        if (size % 2 == 0) {
            return (sortedScores.get(size / 2 - 1) + sortedScores.get(size / 2)) / 2.0;
        } else {
            return sortedScores.get(size / 2).doubleValue();
        }
    }

    public static Integer calculateMode(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0;
        }

        Map<Integer, Long> frequencyMap = scores.stream()
                .collect(Collectors.groupingBy(
                        score -> score,
                        Collectors.counting()
                ));

        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    public static Integer calculateTotal(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0;
        }
        return scores.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static Integer findMax(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0;
        }
        return scores.stream()
                .max(Integer::compareTo)
                .orElse(0);
    }

    public static Integer findMin(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0;
        }
        return scores.stream()
                .min(Integer::compareTo)
                .orElse(0);
    }
}