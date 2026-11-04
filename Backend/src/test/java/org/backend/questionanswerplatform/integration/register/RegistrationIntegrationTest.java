package org.backend.questionanswerplatform.integration.register;
import jakarta.transaction.Transactional;
import org.backend.questionanswerplatform.dto.register.RegisterRequest;
import org.backend.questionanswerplatform.entities.Role;
import org.backend.questionanswerplatform.entities.User;
import org.backend.questionanswerplatform.repositories.RoleRepository;
import org.backend.questionanswerplatform.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RegistrationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setupRoles() {
        Role userole = new Role("USER");
        Role adminRole = new Role("ADMIN");
        roleRepository.save(userole);
        roleRepository.save(adminRole);
    }

    @Test
    void register_validRequest_userSavedToDatabase() throws Exception {
        // building valid request
        RegisterRequest request = new RegisterRequest();
        request.setUserName("testUsername");
        request.setEmail("test@gmail.com");
        request.setPassword("Password1");

        //Fire the http request with mock mvc
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
              .andExpect(status().isCreated());

      //verify if the user in h2
        Optional<User> saved= userRepository.findByEmail("test@gmail.com");
        assertThat(saved).isPresent();
        assertThat(saved.get().getUserName()).isEqualTo("testUsername");
        assertThat(saved.get().getRole().getName()).isEqualTo("USER");
        assertThat(saved.get().getPassword()).isNotEqualTo("Password1");

    }

    @Test
    void register_invalidRequest_returnBadResponse() throws Exception
    {
        // build invalid request
        RegisterRequest request = new RegisterRequest();
        request.setUserName("testUsername");
        request.setEmail("test@");
        request.setPassword("Pass");

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void register_duplicateEmail_returnConflict() throws Exception
    {
        //building valid request
        RegisterRequest request = new RegisterRequest();
        request.setUserName("testUsername");
        request.setEmail("test@gmail.com");
        request.setPassword("Password1");

        //first registration
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        //second registration with the same email

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())// 409
                .andExpect(jsonPath("$.error").value("Email already in use"));

    }


}
