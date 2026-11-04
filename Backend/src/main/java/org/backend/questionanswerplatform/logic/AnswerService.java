package org.backend.questionanswerplatform.logic;

import org.backend.questionanswerplatform.dto.answer.AnswerResponse;
import org.backend.questionanswerplatform.dto.answer.CreateAnswerRequest;
import org.backend.questionanswerplatform.entities.Answer;
import org.backend.questionanswerplatform.entities.Question;
import org.backend.questionanswerplatform.entities.User;
import org.backend.questionanswerplatform.mappers.AnswerMapper;
import org.backend.questionanswerplatform.repositories.AnswerRepository;
import org.backend.questionanswerplatform.repositories.QuestionRepository;
import org.backend.questionanswerplatform.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private static final Logger log = LoggerFactory.getLogger(AnswerService.class);
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerMapper answerMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public AnswerService(AnswerRepository answerRepository,
                         QuestionRepository questionRepository,
                         UserRepository userRepository,
                         AnswerMapper answerMapper,
                         SimpMessagingTemplate messagingTemplate) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.answerMapper = answerMapper;
        this.messagingTemplate=messagingTemplate;
    }

    public AnswerResponse createAnswer(Long questionId, CreateAnswerRequest request, String email)
    {
        Question question= questionRepository.findById(questionId)
                .orElseThrow(()-> new RuntimeException("Question not found"));
        User currentUser= userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalStateException("Authenticated user not found"));
        Answer answer= new Answer(
                request.getBody(),
                question,
                currentUser
        );
        Answer saved= answerRepository.save(answer);
        AnswerResponse response = answerMapper.toResponse(saved);

        log.info("Broadcasting answer to /topic/questions/{}/answers", questionId);
        messagingTemplate.convertAndSend("/topic/questions/" + questionId + "/answers", response);
        log.info("Broadcast done");


        return response;

    }

    public List<AnswerResponse> getAnswersForQuestion(Long questionId)
    {
        return answerRepository.findByQuestionIdOrderByCreatedAtAsc(questionId).stream().map(answerMapper::toResponse).toList();
    }

}
