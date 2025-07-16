package com.studentscoringapp.controller;

import com.studentscoringapp.dto.ReportDto;
import com.studentscoringapp.dto.PagedResponse;
import com.studentscoringapp.service.ReportService;
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
@Tag(name = "Report Management", description = "APIs for generating student score reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Generate student report",
            description = "Generates a comprehensive report for a specific student including mean, median, and mode scores")
    public ResponseEntity<ReportDto> generateStudentReport(
            @Parameter(description = "Student ID") @PathVariable Long studentId) {
        ReportDto report = reportService.generateStudentReport(studentId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/all")
    @Operation(summary = "Generate all students report",
            description = "Generates reports for all students with pagination and filtering options")
    public ResponseEntity<PagedResponse<ReportDto>> generateAllStudentsReport(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "studentId") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Filter by first name") @RequestParam(required = false) String firstName,
            @Parameter(description = "Filter by last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Filter by email") @RequestParam(required = false) String email) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponse<ReportDto> reports = (firstName != null || lastName != null || email != null) ?
                reportService.generateFilteredReport(firstName, lastName, email, pageable) :
                reportService.generateAllStudentsReport(pageable);

        return ResponseEntity.ok(reports);
    }
}