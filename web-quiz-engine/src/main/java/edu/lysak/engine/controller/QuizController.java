package edu.lysak.engine.controller;

import edu.lysak.engine.model.*;
import edu.lysak.engine.service.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable Long id) {
        return quizService.getQuiz(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Page<Quiz> getAllQuizzes(
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        return quizService.getAllQuizzes(page);
    }

    @GetMapping("/completed")
    public Page<QuizCompleted> getAllCompletedQuizzes(
            @RequestParam(required = false, defaultValue = "0") int page,
            @AuthenticationPrincipal User user
    ) {
        return quizService.getAllCompletedQuizzes(page, user);
    }

    @PostMapping("/{id}/solve")
    public QuizResult solveQuiz(
            @PathVariable Long id,
            @RequestBody UserAnswer userAnswer,
            @AuthenticationPrincipal User user) {
        Quiz quiz = quizService.getQuiz(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return quizService.getResult(quiz, userAnswer, user);
    }

    @PostMapping
    public Quiz createQuiz(
            @Valid @RequestBody Quiz quiz,
            @AuthenticationPrincipal User user
    ) {
        return quizService.createQuiz(quiz, user);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        if (!quizService.isQuizExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!quizService.isQuizAuthor(id, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        quizService.deleteQuiz(id);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
