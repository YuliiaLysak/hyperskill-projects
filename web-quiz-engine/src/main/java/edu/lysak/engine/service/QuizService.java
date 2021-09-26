package edu.lysak.engine.service;

import edu.lysak.engine.model.*;
import edu.lysak.engine.repository.QuizCompletedRepository;
import edu.lysak.engine.repository.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private static final int PAGE_SIZE = 10;
    private final QuizRepository quizRepository;
    private final QuizCompletedRepository quizCompletedRepository;

    public QuizService(QuizRepository quizRepository, QuizCompletedRepository quizCompletedRepository) {
        this.quizRepository = quizRepository;
        this.quizCompletedRepository = quizCompletedRepository;
    }

    public Optional<Quiz> getQuiz(@PathVariable Long id) {
        return quizRepository.findById(id);
    }

    public Page<Quiz> getAllQuizzes(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        return quizRepository.findAll(pageable);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Page<QuizCompleted> getAllCompletedQuizzes(int pageNo, User user) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        return quizCompletedRepository.findAllCompletedQuizzes(user.getId(), pageable);
    }

    public Quiz createQuiz(Quiz quiz, User user) {
        quiz.setUser(user);
        return quizRepository.save(quiz);
    }

    public QuizResult getResult(Quiz quiz, UserAnswer userAnswer, User user) {
        if (checkAnswers(quiz.getAnswer(), userAnswer.getAnswer())) {
            saveCompleted(quiz, user);
            return QuizResult.SUCCESS;
        } else {
            return QuizResult.FAIL;
        }
    }

    private void saveCompleted(Quiz quiz, User user) {
        QuizCompleted quizCompleted = new QuizCompleted();
        quizCompleted.setId(quiz.getId());
        quizCompleted.setUserId(user.getId());
        quizCompleted.setCompletedAt(LocalDateTime.now());
        quizCompletedRepository.save(quizCompleted);
    }

    public boolean isQuizExists(Long id) {
        return quizRepository.existsById(id);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public boolean isQuizAuthor(Long quizId, User user) {
        Quiz quiz = quizRepository.findById(quizId).get();
        return quiz.getUser().getId().equals(user.getId());
    }

    private boolean checkAnswers(List<Integer> correctAnswers, List<Integer> userAnswers) {
        if (correctAnswers == null) {
            correctAnswers = List.of();
        }

        if (userAnswers == null) {
            userAnswers = List.of();
        }

        if (userAnswers.size() != correctAnswers.size()) {
            return false;
        }
        List<Boolean> result = userAnswers.stream()
                .map(correctAnswers::contains)
                .filter(it -> it)
                .collect(Collectors.toList());
        return result.size() == correctAnswers.size();
    }
}
