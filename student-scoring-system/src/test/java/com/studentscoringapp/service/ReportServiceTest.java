package com.studentscoringapp.service;

import com.studentscoringapp.dto.ReportDto;
import com.studentscoringapp.dto.PagedResponse;
import com.studentscoringapp.entity.Student;
import com.studentscoringapp.entity.Score;
import com.studentscoringapp.exception.ResourceNotFoundException;
import com.studentscoringapp.repository.StudentRepository;
import com.studentscoringapp.repository.ScoreRepository;
import com.studentscoringapp.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Report Service Tests")
class ReportServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Student student;
    private List<Score> scores;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .studentId("ST001")
                .build();

        scores = Arrays.asList(
                Score.builder().id(1L).student(student).subject("Mathematics").score(85).build(),
                Score.builder().id(2L).student(student).subject("English").score(90).build(),
                Score.builder().id(3L).student(student).subject("Science").score(78).build(),
                Score.builder().id(4L).student(student).subject("History").score(92).build(),
                Score.builder().id(5L).student(student).subject("Geography").score(88).build()
        );
    }

    @Test
    @DisplayName("Should generate student report successfully")
    void shouldGenerateStudentReportSuccessfully() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(scoreRepository.findByStudentId(1L)).thenReturn(scores);

        // When
        ReportDto result = reportService.generateStudentReport(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getStudentId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());

        // Verify subject scores
        assertEquals(5, result.getSubjectScores().size());
        assertEquals(85, result.getSubjectScores().get("Mathematics"));
        assertEquals(90, result.getSubjectScores().get("English"));
        assertEquals(78, result.getSubjectScores().get("Science"));
        assertEquals(92, result.getSubjectScores().get("History"));
        assertEquals(88, result.getSubjectScores().get("Geography"));

        // Verify statistical calculations
        assertEquals(86.6, result.getMeanScore(), 0.1);
        assertEquals(88.0, result.getMedianScore(), 0.1);
        assertEquals(433, result.getTotalScore());
        assertEquals(92, result.getHighestScore());
        assertEquals(78, result.getLowestScore());

        verify(studentRepository).findById(1L);
        verify(scoreRepository).findByStudentId(1L);
    }

    @Test
    @DisplayName("Should throw exception for non-existent student")
    void shouldThrowExceptionForNonExistentStudent() {
        // Given
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> reportService.generateStudentReport(999L));

        assertEquals("Student not found with ID: 999", exception.getMessage());
        verify(studentRepository).findById(999L);
        verify(scoreRepository, never()).findByStudentId(anyLong());
    }
}