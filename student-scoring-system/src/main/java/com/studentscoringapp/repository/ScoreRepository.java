package com.studentscoringapp.repository;

import com.studentscoringapp.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByStudentId(Long studentId);

    List<Score> findBySubject(String subject);

    @Query("SELECT s FROM Score s WHERE s.student.id = :studentId")
    List<Score> findScoresByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT s FROM Score s WHERE s.student.id = :studentId AND s.subject = :subject")
    List<Score> findByStudentIdAndSubject(@Param("studentId") Long studentId,
                                          @Param("subject") String subject);
}