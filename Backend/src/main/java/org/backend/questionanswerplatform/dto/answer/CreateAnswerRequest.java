package org.backend.questionanswerplatform.dto.answer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAnswerRequest {

    @NotBlank(message ="Answer body cannot be empty")
    @Size(min = 5, max = 5000, message = "Answer must be at least 5 letters and max 5000")
    private String body;

    public String getBody(){return body;}

    public void setBody(String body) {
        this.body = body;
    }
}
