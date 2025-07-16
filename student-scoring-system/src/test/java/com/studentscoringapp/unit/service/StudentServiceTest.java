package com.studentscoringapp.unit.service;


import com.studentscoringapp.application.dto.StudentDTO;
import com.studentscoringapp.application.mapper.StudentMapper;
import com.studentscoringapp.domain.entity.Student;
import com.studentscoringapp.domain.repository.StudentRepository;
import com.studentscoringapp.domain.service.impl.StudentServiceImpl;
import com.studentscoringapp.infrastructure.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .studentId("STU001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        studentDTO = StudentDTO.builder()
                .id(1L)
                .studentId("STU001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void createStudent_Success() {
        // Given
        when(studentMapper.toEntity(any(StudentDTO.class))).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.toDTO(any(Student.class))).thenReturn(studentDTO);

        // When
        StudentDTO result = studentService.createStudent(studentDTO);

        // Then
        assertNotNull(result);
        assertEquals(studentDTO.getStudentId(), result.getStudentId());
        assertEquals(studentDTO.getFirstName(), result.getFirstName());
        assertEquals(studentDTO.getLastName(), result.getLastName());

        verify(studentMapper).toEntity(studentDTO);
        verify(studentRepository).save(student);
        verify(studentMapper).toDTO(student);
    }

    @Test
    void getStudentByStudentId_Success() {
        // Given
        when(studentRepository.findByStudentId(anyString())).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(any(Student.class))).thenReturn(studentDTO);

        // When
        StudentDTO result = studentService.getStudentByStudentId("STU001");

        // Then
        assertNotNull(result);
        assertEquals(studentDTO.getStudentId(), result.getStudentId());

        verify(studentRepository).findByStudentId("STU001");
        verify(studentMapper).toDTO(student);
    }

    @Test
    void getStudentByStudentId_NotFound() {
        // Given
        when(studentRepository.findByStudentId(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentByStudentId("STU999");
        });

        verify(studentRepository).findByStudentId("STU999");
        verifyNoInteractions(studentMapper);
    }
}