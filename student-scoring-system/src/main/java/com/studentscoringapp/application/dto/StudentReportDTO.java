package com.studentscoringapp.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Student report with scores and statistics")
public class StudentReportDTO {

    @Schema(description = "Student information")
    private StudentDTO student;

    @Schema(description = "Scores by subject")
    private Map<String, Double> subjectScores;

    @Schema(description = "Mean score")
    private Double meanScore;

    @Schema(description = "Median score")
    private Double medianScore;

    @Schema(description = "Mode score(s)")
    private List<Double> modeScores;

    @Schema(description = "Total subjects")
    private Integer totalSubjects;

    @Schema(description = "Highest score")
    private Double highestScore;

    @Schema(description = "Lowest score")
    private Double lowestScore;
}