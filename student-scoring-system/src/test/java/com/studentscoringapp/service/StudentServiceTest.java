package com.studentscoringapp.service;

import com.studentscoringapp.dto.StudentRequestDto;
import com.studentscoringapp.dto.StudentResponseDto;
import com.studentscoringapp.entity.Student;
import com.studentscoringapp.entity.Score;
import com.studentscoringapp.exception.ResourceNotFoundException;
import com.studentscoringapp.exception.ValidationException;
import com.studentscoringapp.repository.StudentRepository;
import com.studentscoringapp.repository.ScoreRepository;
import com.studentscoringapp.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Student Service Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private StudentRequestDto studentRequestDto;
    private Student student;
    private List<Score> scores;

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
    @DisplayName("Should create student successfully")
    void shouldCreateStudentSuccessfully() {
        // Given
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByStudentId(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(scoreRepository.saveAll(anyList())).thenReturn(scores);
        when(scoreRepository.findByStudentId(anyLong())).thenReturn(scores);

        // When
        StudentResponseDto result = studentService.createStudent(studentRequestDto);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("ST001", result.getStudentId());
        assertEquals(5, result.getSubjects().size());

        verify(studentRepository).save(any(Student.class));
        verify(scoreRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should throw validation exception for duplicate email")
    void shouldThrowValidationExceptionForDuplicateEmail() {
        // Given
        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> studentService.createStudent(studentRequestDto));

        assertEquals("Email already exists: john.doe@example.com", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw validation exception for invalid score")
    void shouldThrowValidationExceptionForInvalidScore() {
        // Given
        Map<String, Integer> invalidSubjects = new HashMap<>();
        invalidSubjects.put("Mathematics", 150); // Invalid score > 100

        studentRequestDto.setSubjects(invalidSubjects);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> studentService.createStudent(studentRequestDto));

        assertTrue(exception.getMessage().contains("Score for subject 'Mathematics' must be between 0 and 100"));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should get student by ID successfully")
    void shouldGetStudentByIdSuccessfully() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(scoreRepository.findByStudentId(1L)).thenReturn(scores);

        // When
        StudentResponseDto result = studentService.getStudentById(1L);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(studentRepository).findById(1L);
        verify(scoreRepository).findByStudentId(1L);
    }

    @Test
    @DisplayName("Should throw resource not found exception for non-existent student")
    void shouldThrowResourceNotFoundExceptionForNonExistentStudent() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> studentService.getStudentById(1L));

        assertEquals("Student not found with ID: 1", exception.getMessage());
        verify(scoreRepository, never()).findByStudentId(anyLong());
    }

    @Test
    @DisplayName("Should update student successfully")
    void shouldUpdateStudentSuccessfully() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(scoreRepository.findByStudentId(1L)).thenReturn(scores);
        when(scoreRepository.saveAll(anyList())).thenReturn(scores);

        // When
        StudentResponseDto result = studentService.updateStudent(1L, studentRequestDto);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(studentRepository).save(any(Student.class));
        verify(scoreRepository).deleteAll(anyList());
        verify(scoreRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should delete student successfully")
    void shouldDeleteStudentSuccessfully() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // When
        studentService.deleteStudent(1L);

        // Then
        verify(studentRepository).findById(1L);
        verify(studentRepository).delete(student);
    }
}