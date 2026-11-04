package org.backend.questionanswerplatform.dto.answer;

import java.time.LocalDateTime;

public class AnswerResponse {

    private Long id;
    private String body;
    private String authorUserName;
    private LocalDateTime createdAt;

    public AnswerResponse(){
        // No-argument constructor required for JSON deserialization (Jackson)
        // A parameterized constructor was added, so this must be explicitly declared
    }

    public AnswerResponse(Long id, String body, String authorUserName, LocalDateTime createdAt) {
        this.id = id;
        this.body = body;
        this.authorUserName = authorUserName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getAuthorUserName() {
        return authorUserName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

