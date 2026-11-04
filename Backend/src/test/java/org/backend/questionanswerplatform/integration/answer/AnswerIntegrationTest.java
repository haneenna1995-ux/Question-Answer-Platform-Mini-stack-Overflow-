package org.backend.questionanswerplatform.integration.answer;

import jakarta.transaction.Transactional;
import org.backend.questionanswerplatform.dto.answer.CreateAnswerRequest;
import org.backend.questionanswerplatform.entities.Question;
import org.backend.questionanswerplatform.entities.Role;
import org.backend.questionanswerplatform.entities.User;
import org.backend.questionanswerplatform.repositories.AnswerRepository;
import org.backend.questionanswerplatform.repositories.QuestionRepository;
import org.backend.questionanswerplatform.repositories.RoleRepository;
import org.backend.questionanswerplatform.repositories.UserRepository;
import org.backend.questionanswerplatform.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AnswerIntegrationTest {

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
    private AnswerRepository answerRepository;

    @Autowired
    private JwtService jwtService;

    private String userToken;
    private Long questionId;

    @BeforeEach
    void setup() {
        Role userRole = new Role("USER");
        roleRepository.save(userRole);

        User user = new User("test", "test@example.com", "Password1", userRole);
        userRepository.save(user);

        userToken = jwtService.generateToken("test@example.com", "USER");

        Question question = new Question("learning java", "How to learn java", user, Set.of());
        questionRepository.save(question);
        questionId = question.getId();
    }

    @Test
    void createAnswer_validRequest_AnswerSavedToDatabase() throws Exception{

        CreateAnswerRequest request = new CreateAnswerRequest();
        request.setBody("This is my answer to the question");
        mockMvc.perform(post("/questions/" + questionId + "/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " +userToken ).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body").value("This is my answer to the question"))
                .andExpect(jsonPath("$.authorUserName").value("test"));

        assertThat(answerRepository.findAll()).hasSize(1);
        assertThat(answerRepository.findAll().get(0).getBody())
                .isEqualTo("This is my answer to the question");

    }

    @Test
    void createAnswer_unAuthenticatedUser_returnUnauthorizedUser() throws Exception{

      CreateAnswerRequest request= new CreateAnswerRequest();
      request.setBody("This is my answer");

      mockMvc.perform(post("/questions/" +questionId+ "/answers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
              .andExpect(status().isUnauthorized())
              .andExpect(jsonPath("$.error").value("Authentication required. Please log in."));

    }

}
