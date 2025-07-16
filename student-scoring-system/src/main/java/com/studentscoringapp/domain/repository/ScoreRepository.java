package com.studentscoringapp.domain.repository;

import com.studentscoringapp.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByStudent(Student student);

    @Query("SELECT s FROM Score s JOIN FETCH s.subject WHERE s.student = :student")
    List<Score> findByStudentWithSubject(@Param("student") Student student);

    Optional<Score> findByStudentAndSubjectName(Student student, String subjectName);

}