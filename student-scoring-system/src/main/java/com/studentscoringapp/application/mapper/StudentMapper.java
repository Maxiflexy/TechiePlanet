package com.studentscoringapp.application.mapper;

import com.studentscoringapp.application.dto.StudentDTO;
import com.studentscoringapp.domain.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }

        return StudentDTO.builder()
                .id(student.getId())
                .studentId(student.getStudentId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    public Student toEntity(StudentDTO dto) {
        if (dto == null) {
            return null;
        }

        return Student.builder()
                .id(dto.getId())
                .studentId(dto.getStudentId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();
    }
}