package org.backend.questionanswerplatform.logic;

import org.backend.questionanswerplatform.dto.question.CreateQuestionRequest;
import org.backend.questionanswerplatform.dto.question.QuestionResponse;
import org.backend.questionanswerplatform.entities.Question;
import org.backend.questionanswerplatform.entities.Tag;
import org.backend.questionanswerplatform.entities.User;
import org.backend.questionanswerplatform.mappers.QuestionMapper;
import org.backend.questionanswerplatform.repositories.QuestionRepository;
import org.backend.questionanswerplatform.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
 class QuestionServiceTest {
    @Mock private QuestionRepository questionRepository;
    @Mock private TagService tagService;
    @Mock private UserRepository userRepository;
    @Mock private QuestionMapper questionMapper;

    @InjectMocks private QuestionService questionService;

 @Test
    void createQuestion_savesQuestionWithCorrectFields_andReturnMappedResponse()
 {
     // Arrange
     String email = "haneen@test.com";
     CreateQuestionRequest request=new CreateQuestionRequest("How does JPA work?","I want to understand entity managers.",List.of(1L,2L));
     Set<Tag> resolvedTags = Set.of(new Tag(), new Tag());
     User author=new User();

     QuestionResponse expectedResponse = new QuestionResponse(
             1L,
             "How does JPA work?",
             "I want to understand entity managers.",
             "Haneen",
             LocalDateTime.now(),
             List.of(),
             0
     );

     when(tagService.resolveTags(List.of(1L,2L))).thenReturn(resolvedTags);
     when(userRepository.findByEmail(email)).thenReturn(Optional.of(author));
     when(questionRepository.save(any(Question.class))).thenAnswer(invocation->invocation.getArgument(0));
     when(questionMapper.toResponse(any(Question.class))).thenReturn(expectedResponse);

     // Act

     QuestionResponse result= questionService.createQuestion(request,email);

     // Assert
     ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
     verify(questionRepository).save(captor.capture());
     Question saved= captor.getValue();

     assertEquals("How does JPA work?", saved.getTitle());
     assertEquals("I want to understand entity managers.", saved.getDescription());
     assertSame(author, saved.getAuthor());
     assertEquals(resolvedTags, saved.getTags());
     assertSame(expectedResponse, result);



 }
    @Test
    void createQuestion_throwsIllegalStateException_whenUserNotFound() {
        // Arrange
        String email = "ghost@test.com";
        CreateQuestionRequest request = new CreateQuestionRequest(
                "Some title", "Some description", List.of(1L));

        when(tagService.resolveTags(List.of(1L))).thenReturn(Set.of());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> questionService.createQuestion(request, email));
    }@Test
    void getAllQuestions_returnsMappedListOfResponses() {
        // Arrange
        Question q1 = new Question();
        Question q2 = new Question();
        QuestionResponse r1 = new QuestionResponse(1L, "Title 1", "Body 1", "Haneen", LocalDateTime.now(), List.of(), 0);
        QuestionResponse r2 = new QuestionResponse(2L, "Title 2", "Body 2", "Haneen", LocalDateTime.now(), List.of(), 0);

        when(questionRepository.findAll()).thenReturn(List.of(q1, q2));
        when(questionMapper.toResponse(q1)).thenReturn(r1);
        when(questionMapper.toResponse(q2)).thenReturn(r2);

        // Act
        List<QuestionResponse> result = questionService.getAllQuestions();

        // Assert
        assertEquals(2, result.size());
        assertEquals(r1, result.get(0));
        assertEquals(r2, result.get(1));
    }

    @Test
    void getAllQuestions_returnsEmptyList_whenNoQuestionsExist() {
        // Arrange
        when(questionRepository.findAll()).thenReturn(List.of());

        // Act
        List<QuestionResponse> result = questionService.getAllQuestions();

        // Assert
        assertEquals(0, result.size());
        verify(questionMapper, never()).toResponse(any());
    }



}


