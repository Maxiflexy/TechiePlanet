package com.studentscoringapp.infrastructure.controller;


import com.studentscoringapp.application.dto.PagedResponseDTO;
import com.studentscoringapp.application.dto.StudentReportDTO;
import com.studentscoringapp.domain.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Report Management", description = "Operations for generating student reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/students")
    @Operation(summary = "Generate student reports", description = "Generates paginated student reports with scores and statistics")
    public ResponseEntity<PagedResponseDTO<StudentReportDTO>> generateStudentReports(
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

        PagedResponseDTO<StudentReportDTO> reports = reportService.generateStudentReports(
                firstName, lastName, email, pageable);

        return ResponseEntity.ok(reports);
    }

    @GetMapping("/students/{studentId}")
    @Operation(summary = "Generate student report", description = "Generates a detailed report for a specific student")
    public ResponseEntity<StudentReportDTO> generateStudentReport(@PathVariable String studentId) {
        StudentReportDTO report = reportService.generateStudentReport(studentId);
        return ResponseEntity.ok(report);
    }
}