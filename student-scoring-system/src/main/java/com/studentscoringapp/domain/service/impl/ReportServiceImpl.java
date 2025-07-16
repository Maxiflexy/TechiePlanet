package com.studentscoringapp.domain.service.impl;


import com.studentscoringapp.application.dto.PagedResponseDTO;
import com.studentscoringapp.application.dto.StudentReportDTO;
import com.studentscoringapp.application.mapper.StudentMapper;
import com.studentscoringapp.domain.entity.Score;
import com.studentscoringapp.domain.entity.Student;
import com.studentscoringapp.domain.repository.ScoreRepository;
import com.studentscoringapp.domain.repository.StudentRepository;
import com.studentscoringapp.domain.service.ReportService;
import com.studentscoringapp.infrastructure.exception.StudentNotFoundException;
import com.studentscoringapp.util.StatisticsCalculator;
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
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final StudentMapper studentMapper;

    @Override
    public PagedResponseDTO<StudentReportDTO> generateStudentReports(String firstName, String lastName, String email, Pageable pageable) {
        log.info("Generating student reports with filters - firstName: {}, lastName: {}, email: {}", firstName, lastName, email);

        Page<Student> studentPage = studentRepository.findStudentsWithFilters(firstName, lastName, email, pageable);

        List<StudentReportDTO> reports = studentPage.getContent().stream()
                .map(this::generateReportForStudent)
                .collect(Collectors.toList());

        return PagedResponseDTO.<StudentReportDTO>builder()
                .content(reports)
                .pageNumber(studentPage.getNumber())
                .pageSize(studentPage.getSize())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .first(studentPage.isFirst())
                .last(studentPage.isLast())
                .build();
    }

    @Override
    public StudentReportDTO generateStudentReport(String studentId) {
        log.info("Generating report for student: {}", studentId);

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + studentId));

        return generateReportForStudent(student);
    }

    private StudentReportDTO generateReportForStudent(Student student) {
        List<Score> scores = scoreRepository.findByStudentWithSubject(student);

        Map<String, Double> subjectScores = scores.stream()
                .collect(Collectors.toMap(
                        score -> score.getSubject().getName(),
                        Score::getValue
                ));

        List<Double> scoreValues = scores.stream()
                .map(Score::getValue)
                .collect(Collectors.toList());

        return StudentReportDTO.builder()
                .student(studentMapper.toDTO(student))
                .subjectScores(subjectScores)
                .meanScore(StatisticsCalculator.calculateMean(scoreValues))
                .medianScore(StatisticsCalculator.calculateMedian(scoreValues))
                .modeScores(StatisticsCalculator.calculateMode(scoreValues))
                .totalSubjects(scoreValues.size())
                .highestScore(StatisticsCalculator.findHighest(scoreValues))
                .lowestScore(StatisticsCalculator.findLowest(scoreValues))
                .build();
    }
}