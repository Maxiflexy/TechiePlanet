package com.studentscoringapp.infrastructure.controller;


import com.studentscoringapp.application.dto.PagedResponseDTO;
import com.studentscoringapp.application.dto.StudentDTO;
import com.studentscoringapp.domain.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "Operations for managing students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Create a new student", description = "Creates a new student in the system")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieves a student by their database ID")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/student-id/{studentId}")
    @Operation(summary = "Get student by student ID", description = "Retrieves a student by their unique student ID")
    public ResponseEntity<StudentDTO> getStudentByStudentId(@PathVariable String studentId) {
        StudentDTO student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    @Operation(summary = "Get students with filters", description = "Retrieves students with optional filters and pagination")
    public ResponseEntity<PagedResponseDTO<StudentDTO>> getStudents(
            @Parameter(description = "Filter by first name") @RequestParam(required = false) String firstName,
            @Parameter(description = "Filter by last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Filter by email") @RequestParam(required = false) String email,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponseDTO<StudentDTO> students = studentService.getStudentsWithFilters(
                firstName, lastName, email, pageable);

        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Updates an existing student")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Deletes a student from the system")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}