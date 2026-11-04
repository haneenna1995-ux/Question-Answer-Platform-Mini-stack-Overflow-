package org.backend.questionanswerplatform.logic;
import org.backend.questionanswerplatform.dto.question.CreateQuestionRequest;
import org.backend.questionanswerplatform.dto.question.QuestionResponse;
import org.backend.questionanswerplatform.entities.Question;
import org.backend.questionanswerplatform.entities.Tag;
import org.backend.questionanswerplatform.entities.User;
import org.backend.questionanswerplatform.mappers.QuestionMapper;
import org.backend.questionanswerplatform.repositories.QuestionRepository;
import org.backend.questionanswerplatform.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagService tagService;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;
    private static final String QUESTION_NOT_FOUND ="Question not found";

    public QuestionService(QuestionRepository questionRepository, TagService tagService,UserRepository userRepository,QuestionMapper questionMapper)
    {
        this.questionRepository= questionRepository;
        this.tagService=tagService;
        this.userRepository=userRepository;
        this.questionMapper=questionMapper;
    }


    public QuestionResponse createQuestion(CreateQuestionRequest request, String email)
    {     Set<Tag> tags = tagService.resolveTags(request.getTagIds());
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        Question question = new Question(request.getTitle(),request.getDescription(),currentUser,tags);

        return questionMapper.toResponse( questionRepository.save(question));
    }


    public List<QuestionResponse> getAllQuestions()
    {
      return questionRepository.findAll().stream().map(questionMapper::toResponse).toList();
    }


    public QuestionResponse getQuestionById(Long id)
    {
        Question question= questionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(QUESTION_NOT_FOUND));
        return questionMapper.toResponse(question);
    }


    public List<QuestionResponse> getMyQuestions(String email)
    {
         return questionRepository.findByAuthorEmail(email).stream().map(questionMapper::toResponse).toList();
    }


    public QuestionResponse updateQuestion( Long id,CreateQuestionRequest request,
                                            String email,
                                            String role)
    {
        Question question = questionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(QUESTION_NOT_FOUND));
        boolean isAdmin= role.equals("ADMIN");
        boolean isAuthor= question.getAuthor().getEmail().equals(email);

        if (!isAdmin && !isAuthor)
        {
            throw new AccessDeniedException("You can only edit you own question");
        }
            question.setTitle(request.getTitle());
            question.setDescription(request.getDescription());
            question.setTags(tagService.resolveTags(request.getTagIds()));

            return questionMapper.toResponse(questionRepository.save(question));

    }


    public void deleteQuestion(Long questionId, String email, String role) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND));

        if (role.equals("ADMIN")) {
            questionRepository.delete(question);
            return;
        }

        if (!question.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("You can only delete your own questions");
        }

        questionRepository.delete(question);
    }







}
