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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {
    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnswerMapper answerMapper;

    @InjectMocks
    private AnswerService answerService;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    //Happy path
    @Test
    void createAnswer_shouldReturnResponse_whenValidInputGiven()
    {
        // Arrange : setup fake data
        Long questionId=1L;
        String userEmail="haneen@example.com";
        CreateAnswerRequest request= new CreateAnswerRequest();
        request.setBody("This is my answer");

        Question fakeQuestion= new Question();
        User fakekUser= new User();
        fakekUser.setEmail(userEmail);

        Answer savedAnswer= new Answer("This is my answer",fakeQuestion,fakekUser);
        AnswerResponse expectedResponse= new AnswerResponse(1L,"This is my answer","haneen", LocalDateTime.now());

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(fakeQuestion));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(fakekUser));
        when(answerRepository.save(any(Answer.class))).thenReturn(savedAnswer);
        when(answerMapper.toResponse(savedAnswer)).thenReturn(expectedResponse);

        // act
        AnswerResponse result = answerService.createAnswer(questionId, request, userEmail);

        // assert
        assertThat(result.getBody()).isEqualTo(expectedResponse.getBody());
        assertThat(result.getAuthorUserName()).isEqualTo(expectedResponse.getAuthorUserName());
        assertThat(result.getId()).isEqualTo(expectedResponse.getId());

        verify(answerRepository).save(argThat(answer -> answer.getBody().equals("This is my answer")));// confirm that the answer built correctly before saving
        verify(messagingTemplate).convertAndSend(
                "/topic/questions/1/answers",
                expectedResponse
        );

    }

    @Test
    void createAnswer_throwException_whenUserNotFound() {

        Long questionId= 1L;
        Question question = new Question();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                answerService.createAnswer(questionId, new CreateAnswerRequest(), "unknown@test.com")
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Authenticated user not found");

    }

    @Test
    void createAnswer_throwException_whenQuestionNotFound() {

        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                answerService.createAnswer(99L, new CreateAnswerRequest(), "haneen@example.com")
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Question not found");

    }
}
