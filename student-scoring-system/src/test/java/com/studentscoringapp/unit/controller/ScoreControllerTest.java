package com.studentscoringapp.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentscoringapp.application.dto.StudentScoreRequestDTO;
import com.studentscoringapp.domain.service.ScoreService;
import com.studentscoringapp.infrastructure.controller.ScoreController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScoreController.class)
class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScoreService scoreService;

    @Test
    void submitScores_Success() throws Exception {
        // Given
        Map<String, Double> scores = new HashMap<>();
        scores.put("Mathematics", 85.0);
        scores.put("Physics", 78.0);
        scores.put("Chemistry", 92.0);
        scores.put("Biology", 88.0);
        scores.put("English", 76.0);

        StudentScoreRequestDTO request = StudentScoreRequestDTO.builder()
                .studentId("STU001")
                .scores(scores)
                .build();

        doNothing().when(scoreService).submitScores(any(StudentScoreRequestDTO.class));

        // When & Then
        mockMvc.perform(post("/api/v1/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Scores submitted successfully"));
    }
}