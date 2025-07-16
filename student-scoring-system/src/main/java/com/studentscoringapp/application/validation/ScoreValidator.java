package com.studentscoringapp.application.validation;

import com.studentscoringapp.application.dto.StudentScoreRequestDTO;
import com.studentscoringapp.infrastructure.exception.InvalidScoreException;
import com.studentscoringapp.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ScoreValidator {

    public void validateScoreRequest(StudentScoreRequestDTO request) {
        if (request == null) {
            throw new InvalidScoreException("Score request cannot be null");
        }

        if (request.getScores() == null || request.getScores().isEmpty()) {
            throw new InvalidScoreException("Scores cannot be empty");
        }

        if (request.getScores().size() != Constants.REQUIRED_SUBJECTS_COUNT) {
            throw new InvalidScoreException("Exactly " + Constants.REQUIRED_SUBJECTS_COUNT + " subjects are required");
        }

        for (Map.Entry<String, Double> entry : request.getScores().entrySet()) {
            String subject = entry.getKey();
            Double score = entry.getValue();

            if (subject == null || subject.trim().isEmpty()) {
                throw new InvalidScoreException("Subject name cannot be empty");
            }

            validateScore(score);
        }
    }

    public void validateScore(Double score) {
        if (score == null) {
            throw new InvalidScoreException("Score cannot be null");
        }

        if (score < Constants.MIN_SCORE || score > Constants.MAX_SCORE) {
            throw new InvalidScoreException("Score must be between " + Constants.MIN_SCORE + " and " + Constants.MAX_SCORE);
        }
    }
}