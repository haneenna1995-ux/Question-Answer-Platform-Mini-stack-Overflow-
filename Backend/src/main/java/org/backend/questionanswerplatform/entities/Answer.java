package org.backend.questionanswerplatform.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "Text")
    private String body;
    private LocalDateTime createdAt=LocalDateTime.now();

@ManyToOne
@JoinColumn(name="question_id", nullable = false)
 private Question question;

@ManyToOne
 @JoinColumn(name="user_id", nullable = false)
 private User user;

public Answer(){}

public Answer(String body, Question question, User user)
{
    this.body=body;
    this.question=question;
    this.user= user;
}

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Question getQuestion() {
        return question;
    }

    public User getUser() {
        return user;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
