package com.studentscoringapp.domain.service;

import com.studentscoringapp.application.dto.PagedResponseDTO;
import com.studentscoringapp.application.dto.StudentReportDTO;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    PagedResponseDTO<StudentReportDTO> generateStudentReports(String firstName, String lastName, String email, Pageable pageable);
    StudentReportDTO generateStudentReport(String studentId);
}
