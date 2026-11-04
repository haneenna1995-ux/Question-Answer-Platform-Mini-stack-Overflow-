package org.backend.questionanswerplatform.dto.question;
import jakarta.validation.constraints.*;

import java.util.List;


public class CreateQuestionRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotEmpty(message = "Tags are required")
    @Size(max = 5, message = "A question can have at most 5 tags")
    private List<@Positive Long> tagIds;

    public CreateQuestionRequest() {
        // No-argument constructor required for JSON deserialization (Jackson)
        // A parameterized constructor was added, so this must be explicitly declared
    }

    public CreateQuestionRequest(String title, String description, List<Long> tagIds) {
        this.title = title;
        this.description = description;
        this.tagIds = tagIds;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getTagIds(){return tagIds;}
    public void setTagIds(List<Long> tagIds){this.tagIds=tagIds;}

}

