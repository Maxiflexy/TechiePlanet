package com.studentscoringapp.domain.service;

import com.studentscoringapp.application.dto.StudentScoreRequestDTO;

public interface ScoreService {
    void submitScores(com.studentscoringapp.application.dto.StudentScoreRequestDTO request);
    void updateScore(String studentId, String subjectName, Double score);
    void deleteScore(String studentId, String subjectName);
}