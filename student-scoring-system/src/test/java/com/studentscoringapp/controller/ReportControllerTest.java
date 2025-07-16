package com.studentscoringapp.controller;

import com.studentscoringapp.dto.ReportDto;
import com.studentscoringapp.dto.PagedResponse;
import com.studentscoringapp.exception.ResourceNotFoundException;
import com.studentscoringapp.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@DisplayName("Report Controller Tests")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    private ReportDto reportDto;
    private PagedResponse<ReportDto> pagedResponse;

    @BeforeEach
    void setUp() {
        Map<String, Integer> subjectScores = new HashMap<>();
        subjectScores.put("Mathematics", 85);
        subjectScores.put("English", 90);
        subjectScores.put("Science", 78);
        subjectScores.put("History", 92);
        subjectScores.put("Geography", 88);

        reportDto = ReportDto.builder()
                .studentId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .subjectScores(subjectScores)
                .meanScore(86.6)
                .medianScore(88.0)
                .modeScore(85)
                .totalScore(433)
                .highestScore(92)
                .lowestScore(78)
                .build();

        pagedResponse = PagedResponse.<ReportDto>builder()
                .content(Arrays.asList(reportDto))
                .page(0)
                .size(10)
                .totalElements(1L)
                .totalPages(1)
                .first(true)
                .last(true)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    @Test
    @DisplayName("Should generate student report successfully")
    void shouldGenerateStudentReportSuccessfully() throws Exception {
        // Given
        when(reportService.generateStudentReport(1L)).thenReturn(reportDto);

        // When & Then
        mockMvc.perform(get("/api/v1/reports/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.meanScore").value(86.6))
                .andExpect(jsonPath("$.medianScore").value(88.0))
                .andExpect(jsonPath("$.modeScore").value(85))
                .andExpect(jsonPath("$.totalScore").value(433))
                .andExpect(jsonPath("$.highestScore").value(92))
                .andExpect(jsonPath("$.lowestScore").value(78))
                .andExpect(jsonPath("$.subjectScores.Mathematics").value(85))
                .andExpect(jsonPath("$.subjectScores.English").value(90));
    }

    @Test
    @DisplayName("Should return 404 for non-existent student report")
    void shouldReturn404ForNonExistentStudentReport() throws Exception {
        // Given
        when(reportService.generateStudentReport(anyLong()))
                .thenThrow(new ResourceNotFoundException("Student not found with ID: 999"));

        // When & Then
        mockMvc.perform(get("/api/v1/reports/student/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should generate all students report successfully")
    void shouldGenerateAllStudentsReportSuccessfully() throws Exception {
        // Given
        when(reportService.generateAllStudentsReport(any(Pageable.class)))
                .thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/reports/all")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].studentId").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));
    }
}