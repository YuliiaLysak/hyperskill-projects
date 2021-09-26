package edu.lysak.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizzes_completed")
public class QuizCompleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long completedId;

    @Column
    private Long id;

    @Column
    @JsonIgnore
    private Long userId;

    @Column
    private LocalDateTime completedAt;
}
