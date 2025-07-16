package com.studentscoringapp.service.impl;

import com.studentscoringapp.dto.ReportDto;
import com.studentscoringapp.dto.PagedResponse;
import com.studentscoringapp.entity.Student;
import com.studentscoringapp.entity.Score;
import com.studentscoringapp.exception.ResourceNotFoundException;
import com.studentscoringapp.repository.StudentRepository;
import com.studentscoringapp.repository.ScoreRepository;
import com.studentscoringapp.service.ReportService;
import com.studentscoringapp.util.StatisticsUtil;
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
public class ReportServiceImpl implements ReportService {

    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;

    @Override
    @Transactional(readOnly = true)
    public ReportDto generateStudentReport(Long studentId) {
        log.info("Generating report for student ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<Score> scores = scoreRepository.findByStudentId(studentId);

        return createReportDto(student, scores);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ReportDto> generateAllStudentsReport(Pageable pageable) {
        log.info("Generating report for all students with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Student> studentPage = studentRepository.findAll(pageable);

        List<ReportDto> reports = studentPage.getContent().stream()
                .map(student -> {
                    List<Score> scores = scoreRepository.findByStudentId(student.getId());
                    return createReportDto(student, scores);
                })
                .collect(Collectors.toList());

        return PagedResponse.<ReportDto>builder()
                .content(reports)
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

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ReportDto> generateFilteredReport(String firstName, String lastName,
                                                           String email, Pageable pageable) {
        log.info("Generating filtered report: firstName={}, lastName={}, email={}",
                firstName, lastName, email);

        Page<Student> studentPage = studentRepository.findWithFilters(firstName, lastName, email, pageable);

        List<ReportDto> reports = studentPage.getContent().stream()
                .map(student -> {
                    List<Score> scores = scoreRepository.findByStudentId(student.getId());
                    return createReportDto(student, scores);
                })
                .collect(Collectors.toList());

        return PagedResponse.<ReportDto>builder()
                .content(reports)
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

    private ReportDto createReportDto(Student student, List<Score> scores) {
        Map<String, Integer> subjectScores = scores.stream()
                .collect(Collectors.toMap(Score::getSubject, Score::getScore));

        List<Integer> scoreValues = scores.stream()
                .map(Score::getScore)
                .collect(Collectors.toList());

        return ReportDto.builder()
                .studentId(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .subjectScores(subjectScores)
                .meanScore(StatisticsUtil.calculateMean(scoreValues))
                .medianScore(StatisticsUtil.calculateMedian(scoreValues))
                .modeScore(StatisticsUtil.calculateMode(scoreValues))
                .totalScore(StatisticsUtil.calculateTotal(scoreValues))
                .highestScore(StatisticsUtil.findMax(scoreValues))
                .lowestScore(StatisticsUtil.findMin(scoreValues))
                .build();
    }
}