package com.studentscoringapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;

    // Individual subject scores
    private Map<String, Integer> subjectScores;

    // Statistical calculations
    private Double meanScore;
    private Double medianScore;
    private Integer modeScore;
    private Integer totalScore;
    private Integer highestScore;
    private Integer lowestScore;
}