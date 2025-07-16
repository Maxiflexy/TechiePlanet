package com.studentscoringapp.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for submitting student scores")
public class StudentScoreRequestDTO {

    @NotBlank(message = "Student ID is required")
    @Schema(description = "Student identifier", example = "STU001", required = true)
    private String studentId;

    @NotNull(message = "Scores are required")
    @Schema(description = "Map of subject names to scores", example = "{\"Mathematics\": 85.5, \"Physics\": 78.0, \"Chemistry\": 92.0, \"Biology\": 88.5, \"English\": 76.0}", required = true)
    private Map<String, Double> scores;
}