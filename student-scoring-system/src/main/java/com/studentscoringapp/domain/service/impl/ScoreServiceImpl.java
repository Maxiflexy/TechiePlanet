package com.studentscoringapp.domain.service.impl;


import com.studentscoringapp.application.dto.StudentScoreRequestDTO;
import com.studentscoringapp.application.validation.ScoreValidator;
import com.studentscoringapp.domain.entity.Score;
import com.studentscoringapp.domain.entity.Student;
import com.studentscoringapp.domain.entity.Subject;
import com.studentscoringapp.domain.repository.ScoreRepository;
import com.studentscoringapp.domain.repository.StudentRepository;
import com.studentscoringapp.domain.repository.SubjectRepository;
import com.studentscoringapp.domain.service.ScoreService;
import com.studentscoringapp.infrastructure.exception.InvalidScoreException;
import com.studentscoringapp.infrastructure.exception.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ScoreValidator scoreValidator;

    @Override
    public void submitScores(StudentScoreRequestDTO request) {
        log.info("Submitting scores for student: {}", request.getStudentId());

        // Validate the request
        scoreValidator.validateScoreRequest(request);

        // Find the student
        Student student = studentRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + request.getStudentId()));

        // Process each score
        request.getScores().forEach((subjectName, scoreValue) -> {
            Subject subject = subjectRepository.findByName(subjectName)
                    .orElseThrow(() -> new InvalidScoreException("Subject not found: " + subjectName));

            // Check if score already exists
            scoreRepository.findByStudentAndSubjectName(student, subjectName)
                    .ifPresentOrElse(
                            existingScore -> {
                                existingScore.setValue(scoreValue);
                                scoreRepository.save(existingScore);
                                log.debug("Updated score for student {} in subject {}: {}",
                                        student.getStudentId(), subjectName, scoreValue);
                            },
                            () -> {
                                Score newScore = Score.builder()
                                        .student(student)
                                        .subject(subject)
                                        .value(scoreValue)
                                        .build();
                                scoreRepository.save(newScore);
                                log.debug("Created score for student {} in subject {}: {}",
                                        student.getStudentId(), subjectName, scoreValue);
                            }
                    );
        });

        log.info("Successfully submitted {} scores for student {}",
                request.getScores().size(), request.getStudentId());
    }

    @Override
    public void updateScore(String studentId, String subjectName, Double score) {
        log.info("Updating score for student {} in subject {}: {}", studentId, subjectName, score);

        scoreValidator.validateScore(score);

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + studentId));

        Score existingScore = scoreRepository.findByStudentAndSubjectName(student, subjectName)
                .orElseThrow(() -> new InvalidScoreException("Score not found for student " + studentId + " in subject " + subjectName));

        existingScore.setValue(score);
        scoreRepository.save(existingScore);

        log.info("Successfully updated score for student {} in subject {}", studentId, subjectName);
    }

    @Override
    public void deleteScore(String studentId, String subjectName) {
        log.info("Deleting score for student {} in subject {}", studentId, subjectName);

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + studentId));

        Score score = scoreRepository.findByStudentAndSubjectName(student, subjectName)
                .orElseThrow(() -> new InvalidScoreException("Score not found for student " + studentId + " in subject " + subjectName));

        scoreRepository.delete(score);
        log.info("Successfully deleted score for student {} in subject {}", studentId, subjectName);
    }
}