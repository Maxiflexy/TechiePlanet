package com.studentscoringapp.service;

import com.studentscoringapp.dto.StudentRequestDto;
import com.studentscoringapp.dto.StudentResponseDto;
import com.studentscoringapp.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    StudentResponseDto createStudent(StudentRequestDto studentRequestDto);

    StudentResponseDto getStudentById(Long id);

    StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto);

    void deleteStudent(Long id);

    PagedResponse<StudentResponseDto> getAllStudents(Pageable pageable);

    PagedResponse<StudentResponseDto> getStudentsWithFilters(String firstName, String lastName,
                                                             String email, Pageable pageable);
}