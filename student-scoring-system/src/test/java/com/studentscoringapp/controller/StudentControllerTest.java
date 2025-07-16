package com.studentscoringapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentscoringapp.dto.StudentRequestDto;
import com.studentscoringapp.dto.StudentResponseDto;
import com.studentscoringapp.exception.ResourceNotFoundException;
import com.studentscoringapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@DisplayName("Student Controller Tests")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentRequestDto studentRequestDto;
    private StudentResponseDto studentResponseDto;

    @BeforeEach
    void setUp() {
        Map<String, Integer> subjects = new HashMap<>();
        subjects.put("Mathematics", 85);
        subjects.put("English", 90);
        subjects.put("Science", 78);
        subjects.put("History", 92);
        subjects.put("Geography", 88);

        studentRequestDto = StudentRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .studentId("ST001")
                .subjects(subjects)
                .build();

        studentResponseDto = StudentResponseDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .studentId("ST001")
                .subjects(subjects)
                .build();
    }

    @Test
    @DisplayName("Should create student successfully")
    void shouldCreateStudentSuccessfully() throws Exception {
        // Given
        when(studentService.createStudent(any(StudentRequestDto.class)))
                .thenReturn(studentResponseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.studentId").value("ST001"))
                .andExpect(jsonPath("$.subjects.Mathematics").value(85));
    }

    @Test
    @DisplayName("Should get student by ID successfully")
    void shouldGetStudentByIdSuccessfully() throws Exception {
        // Given
        when(studentService.getStudentById(1L)).thenReturn(studentResponseDto);

        // When & Then
        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Should return 404 for non-existent student")
    void shouldReturn404ForNonExistentStudent() throws Exception {
        // Given
        when(studentService.getStudentById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Student not found with ID: 1"));

        // When & Then
        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 for invalid request data")
    void shouldReturn400ForInvalidRequestData() throws Exception {
        // Given
        StudentRequestDto invalidRequest = StudentRequestDto.builder()
                .firstName("") // Invalid - empty first name
                .lastName("Doe")
                .email("invalid-email") // Invalid email format
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}