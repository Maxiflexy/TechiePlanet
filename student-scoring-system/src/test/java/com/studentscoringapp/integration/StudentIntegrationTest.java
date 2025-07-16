package com.studentscoringapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentscoringapp.application.dto.StudentDTO;
import com.studentscoringapp.domain.entity.Student;
import com.studentscoringapp.domain.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Testcontainers
@Transactional
class StudentIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void createStudent_Success() throws Exception {
        // Given
        StudentDTO studentDTO = StudentDTO.builder()
                .studentId("STU001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value("STU001"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getStudentByStudentId_Success() throws Exception {
        // Given
        Student student = Student.builder()
                .studentId("STU001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
        studentRepository.save(student);

        // When & Then
        mockMvc.perform(get("/api/v1/students/student-id/STU001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("STU001"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getStudentByStudentId_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/students/student-id/STU999"))
                .andExpect(status().isNotFound());
    }
}