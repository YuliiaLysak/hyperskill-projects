package edu.lysak.engine.repository;

import edu.lysak.engine.model.QuizCompleted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCompletedRepository extends JpaRepository<QuizCompleted, Long> {

    @Query("SELECT quiz FROM QuizCompleted quiz" +
            " WHERE quiz.userId = :userId" +
            " ORDER BY quiz.completedAt DESC")
    Page<QuizCompleted> findAllCompletedQuizzes(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
