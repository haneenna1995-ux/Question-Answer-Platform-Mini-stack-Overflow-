package org.backend.questionanswerplatform.integration.register;

import jakarta.transaction.Transactional;
import org.backend.questionanswerplatform.dto.register.LoginRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup()
    {
        Role useRole= new Role("USER");
        roleRepository.save(useRole);

        User user= new User("testName","test@example.com",passwordEncoder.encode("Password1"),useRole);
        userRepository.save(user);
    }

    @Test
    void login_validCredentials_returnTokenAndUserResponse() throws Exception
    {
        // build valid request
        LoginRequest request= new LoginRequest("test@example.com", "Password1");

        //faking the browser
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.user.userName").value("testName"))
                .andExpect(jsonPath("$.user.role").value("USER"));

    }

    @Test
    void login_emailNotExisted_returnException() throws Exception
    {
       LoginRequest request= new LoginRequest("haneen@gmail.com", "Password1");
       mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isUnauthorized()) //401
               .andExpect(jsonPath("$.error").value("Invalid credentials"));

    }

    @Test
    void login_lockedAccount_Return403() throws Exception
    {
        User user= userRepository.findByEmail("test@example.com").orElseThrow();
        user.setLockoutUntil(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        LoginRequest request= new LoginRequest("test@example.com","Password1");
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()) //403
                .andExpect(jsonPath("$.error").value("Account locked. Try again later."));

    }

}
