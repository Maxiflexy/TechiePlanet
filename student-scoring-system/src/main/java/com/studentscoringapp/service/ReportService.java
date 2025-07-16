package com.studentscoringapp.service;

import com.studentscoringapp.dto.ReportDto;
import com.studentscoringapp.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    ReportDto generateStudentReport(Long studentId);

    PagedResponse<ReportDto> generateAllStudentsReport(Pageable pageable);

    PagedResponse<ReportDto> generateFilteredReport(String firstName, String lastName,
                                                    String email, Pageable pageable);
}