package org.backend.questionanswerplatform.integration.question;

import jakarta.transaction.Transactional;
import org.backend.questionanswerplatform.dto.question.CreateQuestionRequest;
import org.backend.questionanswerplatform.entities.Question;
import org.backend.questionanswerplatform.entities.Role;
import org.backend.questionanswerplatform.entities.Tag;
import org.backend.questionanswerplatform.entities.User;
import org.backend.questionanswerplatform.repositories.QuestionRepository;
import org.backend.questionanswerplatform.repositories.RoleRepository;
import org.backend.questionanswerplatform.repositories.TagRepository;
import org.backend.questionanswerplatform.repositories.UserRepository;
import org.backend.questionanswerplatform.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class QuestionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userToken;
    private String secondUserToken;
    private Tag savedTag;
    private Long questionId;

    @BeforeEach
            void setup() {
        Role userRole = new Role("USER");
        roleRepository.save(userRole);

        User user= new User("test","test@example.com","Password1",userRole );
        userRepository.save(user);
        User user2= new User("user2","user2@example.com","Password2",userRole );
        userRepository.save(user2);

        userToken = jwtService.generateToken("test@example.com","USER");
        secondUserToken= jwtService.generateToken("user2@example.com","USER");
        Tag tag= new Tag();
        tag.setName("java");
        savedTag= tagRepository.save(tag);

        Question question= new Question("learning java","How to learn java",user, Set.of(tag));
        questionRepository.save(question);
        questionId= question.getId();

    }

    @Test
    void createQuestion_ValidRequest_QuestionSavedToDatabase() throws  Exception{

                CreateQuestionRequest request = new CreateQuestionRequest(
                "What is Spring Boot?",
                "I want to understand Spring Boot",
                List.of(savedTag.getId())
        );

                mockMvc.perform(post("/questions").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + userToken).content(objectMapper.writeValueAsString(request)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title").value("What is Spring Boot?"))
                        .andExpect(jsonPath("$.description").value("I want to understand Spring Boot"))
                        .andExpect(jsonPath("$.authorUsername").value("test"))
                        .andExpect(jsonPath("$.tags[0].name").value("java"));

                // verify actually saved in the database
          List<Question> questions = questionRepository.findAll();
           assertThat(questions).hasSize(2);
           assertThat(questions.get(1).getTitle()).isEqualTo("What is Spring Boot?");
           assertThat(questions.get(1).getDescription()).isEqualTo("I want to understand Spring Boot");
           assertThat(questions.get(1).getAuthor().getEmail()).isEqualTo("test@example.com");
    }

   // if the  unauthenticated user tries to attach authenticated create question end point
    @Test
    void createQuestion_unauthenticatedUser_returnUnauthorized() throws Exception{
        mockMvc.perform(post("/questions").contentType(MediaType.APPLICATION_JSON))// no header authorization
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Authentication required. Please log in."));

    }

    // happy path for delete question
    @Test
    void deleteQuestion_OwnerDeleteOwnQuestion_questionRemovedFromDatabase() throws Exception
    {
        mockMvc.perform(delete("/questions/delete/" + questionId).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());


        Optional<Question> deleted= questionRepository.findById(questionId);
        assertThat(deleted).isEmpty();

    }
   // unhappy path for delete question
    @Test
    void deleteQuestion_NotOwnerDeleteQuestion_returnDeniedAccessException() throws Exception
    {
        mockMvc.perform(delete("/questions/delete/" + questionId).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + secondUserToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("You can only delete your own questions"));
    }
// unhappy path for delete question
    @Test
    void deleteQuestion_unauthenticatedUser_returnUnauthorizes() throws Exception {
        mockMvc.perform(delete("/questions/delete/"+ questionId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Authentication required. Please log in."));
    }

}
