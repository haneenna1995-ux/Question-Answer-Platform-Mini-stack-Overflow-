package org.backend.questionanswerplatform.mappers;


import org.backend.questionanswerplatform.dto.answer.AnswerResponse;
import org.backend.questionanswerplatform.entities.Answer;
import org.springframework.stereotype.Component;
@Component
public class AnswerMapper {
    public AnswerResponse toResponse(Answer answer){
        return new AnswerResponse(
                answer.getId(),
                answer.getBody(),
                answer.getUser().getUserName(),
                answer.getCreatedAt()
        );


    }
}
