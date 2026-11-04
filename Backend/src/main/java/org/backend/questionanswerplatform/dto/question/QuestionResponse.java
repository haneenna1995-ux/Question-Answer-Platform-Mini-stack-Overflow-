package org.backend.questionanswerplatform.dto.question;

import org.backend.questionanswerplatform.dto.tag.TagResponse;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionResponse {

    private Long id;
    private String title;
    private String description;
    private String authorUsername;
    private LocalDateTime createdAt;
    private List<TagResponse> tags;
    private int answersCount;

    public QuestionResponse(Long id, String title, String body,
                            String authorUsername, LocalDateTime createdAt,
                            List<TagResponse> tags, int answersCount) {
        this.id = id;
        this.title = title;
        this.description = body;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
        this.tags = tags;
        this.answersCount=answersCount;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthorUsername() { return authorUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<TagResponse> getTags() { return tags; }
    public int getAnswersCount() {
        return answersCount;
    }
}


