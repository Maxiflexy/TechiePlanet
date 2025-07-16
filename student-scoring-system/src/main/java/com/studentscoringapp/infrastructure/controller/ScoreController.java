package com.studentscoringapp.infrastructure.controller;

import com.studentscoringapp.application.dto.StudentScoreRequestDTO;
import com.studentscoringapp.domain.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scores")
@RequiredArgsConstructor
@Tag(name = "Score Management", description = "Operations for managing student scores")
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping
    @Operation(summary = "Submit student scores", description = "Submits scores for a student in multiple subjects")
    public ResponseEntity<String> submitScores(@Valid @RequestBody StudentScoreRequestDTO request) {
        scoreService.submitScores(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Scores submitted successfully");
    }

    @PutMapping("/{studentId}/subject/{subjectName}")
    @Operation(summary = "Update single score", description = "Updates a score for a specific student and subject")
    public ResponseEntity<String> updateScore(@PathVariable String studentId,
                                              @PathVariable String subjectName,
                                              @RequestParam Double score) {
        scoreService.updateScore(studentId, subjectName, score);
        return ResponseEntity.ok("Score updated successfully");
    }

    @DeleteMapping("/{studentId}/subject/{subjectName}")
    @Operation(summary = "Delete score", description = "Deletes a score for a specific student and subject")
    public ResponseEntity<String> deleteScore(@PathVariable String studentId,
                                              @PathVariable String subjectName) {
        scoreService.deleteScore(studentId, subjectName);
        return ResponseEntity.ok("Score deleted successfully");
    }
}