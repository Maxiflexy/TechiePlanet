package com.studentscoringapp.domain.service;

import com.studentscoringapp.application.dto.PagedResponseDTO;
import com.studentscoringapp.application.dto.StudentDTO;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);
    StudentDTO getStudentById(Long id);
    StudentDTO getStudentByStudentId(String studentId);
    PagedResponseDTO<StudentDTO> getStudentsWithFilters(String firstName, String lastName, String email, Pageable pageable);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    void deleteStudent(Long id);
}