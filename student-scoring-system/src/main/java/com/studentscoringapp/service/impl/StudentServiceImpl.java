package com.studentscoringapp.service.impl;

import com.studentscoringapp.dto.StudentRequestDto;
import com.studentscoringapp.dto.StudentResponseDto;
import com.studentscoringapp.dto.PagedResponse;
import com.studentscoringapp.entity.Student;
import com.studentscoringapp.entity.Score;
import com.studentscoringapp.exception.ResourceNotFoundException;
import com.studentscoringapp.exception.ValidationException;
import com.studentscoringapp.repository.StudentRepository;
import com.studentscoringapp.repository.ScoreRepository;
import com.studentscoringapp.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;

    @Override
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto) {
        log.info("Creating new student: {} {}", studentRequestDto.getFirstName(), studentRequestDto.getLastName());

        // Validate email uniqueness
        if (studentRequestDto.getEmail() != null &&
                studentRepository.existsByEmail(studentRequestDto.getEmail())) {
            throw new ValidationException("Email already exists: " + studentRequestDto.getEmail());
        }

        // Validate student ID uniqueness
        if (studentRequestDto.getStudentId() != null &&
                studentRepository.existsByStudentId(studentRequestDto.getStudentId())) {
            throw new ValidationException("Student ID already exists: " + studentRequestDto.getStudentId());
        }

        // Validate subject scores
        validateSubjectScores(studentRequestDto.getSubjects());

        // Create student entity
        Student student = Student.builder()
                .firstName(studentRequestDto.getFirstName())
                .lastName(studentRequestDto.getLastName())
                .email(studentRequestDto.getEmail())
                .studentId(studentRequestDto.getStudentId())
                .build();

        Student savedStudent = studentRepository.save(student);

        // Create score entities
        if (studentRequestDto.getSubjects() != null) {
            List<Score> scores = studentRequestDto.getSubjects().entrySet().stream()
                    .map(entry -> Score.builder()
                            .student(savedStudent)
                            .subject(entry.getKey())
                            .score(entry.getValue())
                            .build())
                    .collect(Collectors.toList());

            scoreRepository.saveAll(scores);
        }

        log.info("Student created successfully with ID: {}", savedStudent.getId());
        return convertToResponseDto(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long id) {
        log.info("Fetching student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        return convertToResponseDto(student);
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto) {
        log.info("Updating student with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        // Validate email uniqueness (excluding current student)
        if (studentRequestDto.getEmail() != null &&
                !studentRequestDto.getEmail().equals(existingStudent.getEmail()) &&
                studentRepository.existsByEmail(studentRequestDto.getEmail())) {
            throw new ValidationException("Email already exists: " + studentRequestDto.getEmail());
        }

        // Validate student ID uniqueness (excluding current student)
        if (studentRequestDto.getStudentId() != null &&
                !studentRequestDto.getStudentId().equals(existingStudent.getStudentId()) &&
                studentRepository.existsByStudentId(studentRequestDto.getStudentId())) {
            throw new ValidationException("Student ID already exists: " + studentRequestDto.getStudentId());
        }

        // Validate subject scores
        validateSubjectScores(studentRequestDto.getSubjects());

        // Update student entity
        existingStudent.setFirstName(studentRequestDto.getFirstName());
        existingStudent.setLastName(studentRequestDto.getLastName());
        existingStudent.setEmail(studentRequestDto.getEmail());
        existingStudent.setStudentId(studentRequestDto.getStudentId());

        Student updatedStudent = studentRepository.save(existingStudent);

        // Update scores
        if (studentRequestDto.getSubjects() != null) {
            // Delete existing scores
            List<Score> existingScores = scoreRepository.findByStudentId(id);
            scoreRepository.deleteAll(existingScores);

            // Create new scores
            List<Score> newScores = studentRequestDto.getSubjects().entrySet().stream()
                    .map(entry -> Score.builder()
                            .student(updatedStudent)
                            .subject(entry.getKey())
                            .score(entry.getValue())
                            .build())
                    .collect(Collectors.toList());

            scoreRepository.saveAll(newScores);
        }

        log.info("Student updated successfully with ID: {}", id);
        return convertToResponseDto(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        studentRepository.delete(student);
        log.info("Student deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<StudentResponseDto> getAllStudents(Pageable pageable) {
        log.info("Fetching all students with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Student> studentPage = studentRepository.findAll(pageable);

        return createPagedResponse(studentPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<StudentResponseDto> getStudentsWithFilters(String firstName, String lastName,
                                                                    String email, Pageable pageable) {
        log.info("Fetching students with filters: firstName={}, lastName={}, email={}",
                firstName, lastName, email);

        Page<Student> studentPage = studentRepository.findWithFilters(firstName, lastName, email, pageable);

        return createPagedResponse(studentPage);
    }

    private void validateSubjectScores(Map<String, Integer> subjects) {
        if (subjects != null) {
            subjects.forEach((subject, score) -> {
                if (score < 0 || score > 100) {
                    throw new ValidationException(
                            String.format("Score for subject '%s' must be between 0 and 100. Got: %d",
                                    subject, score));
                }
            });
        }
    }

    private StudentResponseDto convertToResponseDto(Student student) {
        Map<String, Integer> subjectScores = scoreRepository.findByStudentId(student.getId())
                .stream()
                .collect(Collectors.toMap(Score::getSubject, Score::getScore));

        return StudentResponseDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .studentId(student.getStudentId())
                .subjects(subjectScores)
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    private PagedResponse<StudentResponseDto> createPagedResponse(Page<Student> studentPage) {
        List<StudentResponseDto> content = studentPage.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

        return PagedResponse.<StudentResponseDto>builder()
                .content(content)
                .page(studentPage.getNumber())
                .size(studentPage.getSize())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .first(studentPage.isFirst())
                .last(studentPage.isLast())
                .hasNext(studentPage.hasNext())
                .hasPrevious(studentPage.hasPrevious())
                .build();
    }
}