package com.studentscoringapp.domain.service.impl;

import com.studentscoringapp.application.dto.PagedResponseDTO;
import com.studentscoringapp.application.dto.StudentDTO;
import com.studentscoringapp.application.mapper.StudentMapper;
import com.studentscoringapp.domain.entity.Student;
import com.studentscoringapp.domain.repository.StudentRepository;
import com.studentscoringapp.domain.service.StudentService;
import com.studentscoringapp.infrastructure.exception.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating student: {}", studentDTO.getStudentId());

        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);

        log.info("Student created successfully: {}", savedStudent.getId());
        return studentMapper.toDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        log.info("Getting student by id: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentByStudentId(String studentId) {
        log.info("Getting student by student id: {}", studentId);

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with student id: " + studentId));

        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDTO<StudentDTO> getStudentsWithFilters(String firstName, String lastName, String email, Pageable pageable) {
        log.info("Getting students with filters - firstName: {}, lastName: {}, email: {}", firstName, lastName, email);

        Page<Student> studentPage = studentRepository.findStudentsWithFilters(firstName, lastName, email, pageable);

        return PagedResponseDTO.<StudentDTO>builder()
                .content(studentPage.getContent().stream()
                        .map(studentMapper::toDTO)
                        .toList())
                .pageNumber(studentPage.getNumber())
                .pageSize(studentPage.getSize())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .first(studentPage.isFirst())
                .last(studentPage.isLast())
                .build();
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setEmail(studentDTO.getEmail());

        Student updatedStudent = studentRepository.save(existingStudent);

        log.info("Student updated successfully: {}", updatedStudent.getId());
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Deleting student: {}", id);

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
        log.info("Student deleted successfully: {}", id);
    }
}
