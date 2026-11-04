package org.backend.questionanswerplatform.mappers;

import org.backend.questionanswerplatform.dto.question.QuestionResponse;
import org.backend.questionanswerplatform.entities.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    private final TagMapper tagMapper;
    public QuestionMapper(TagMapper tagMapper){
        this.tagMapper=tagMapper;
    }

    public QuestionResponse toResponse(Question q) {
        return new QuestionResponse(
                q.getId(),
                q.getTitle(),
                q.getDescription(),
                q.getAuthor().getUserName(),
                q.getCreatedAt(),
                q.getTags().stream().map(tagMapper::toResponse).toList(),
                q.getAnswers().size()
        );

    }
}

