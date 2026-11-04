package org.backend.questionanswerplatform.repositories;

import org.backend.questionanswerplatform.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findByQuestionIdOrderByCreatedAtAsc(Long questionId);
    Long countByQuestionId(Long questionId);
}


