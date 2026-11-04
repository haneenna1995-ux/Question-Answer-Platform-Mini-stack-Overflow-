package org.backend.questionanswerplatform.integration.question;

import jakarta.transaction.Transactional;
import org.backend.questionanswerplatform.dto.tag.CreateTagRequest;
import org.backend.questionanswerplatform.entities.Role;
import org.backend.questionanswerplatform.entities.Tag;
import org.backend.questionanswerplatform.entities.User;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TagIntegrationTest {

    @Autowired
     private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
     private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String adminToken;
    private String prefix;


    @BeforeEach
    void setup(){
        Role role= new Role("USER");
        roleRepository.save(role);

        Role adminRole = new Role("ADMIN");
        roleRepository.save(adminRole);

        User user= new User("user","user@example.com","Password1", role);
        userRepository.save(user);

        User admin= new User("admin","admin@example.com","Password2", adminRole);
        userRepository.save(admin);

        userToken= jwtService.generateToken("user@example.com","USER");
        adminToken=jwtService.generateToken("admin@example.com","ADMIN");

        Tag tag= new Tag();
        tag.setName("java");
        tagRepository.save(tag);
        prefix= "ja";

    }

    @Test
    void getSuggestion_AuthenticatedUser_returnSuggestedTags() throws Exception{

        mockMvc.perform(get("/tags/suggest?query=" +prefix).contentType(MediaType.APPLICATION_JSON).header("Authorization","Bearer " + userToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value("java"));
    }

    // unhappy path
    @Test
    void getSuggestion_unauthenticatedUser_returnUnauthorized() throws Exception{
        mockMvc.perform(get("/tags/**").contentType(MediaType.APPLICATION_JSON)) //no header
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Authentication required. Please log in."));
    }

    @Test
    void createTag_adminRole_returnIsOke() throws Exception{

        CreateTagRequest request= new  CreateTagRequest();
        request.setName("spring boot");

        mockMvc.perform(post("/tags/admin").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer "+adminToken)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());



    }

    @Test
    void createTag_notAdminRole_returnForbidden() throws Exception {
        CreateTagRequest request = new CreateTagRequest();
        request.setName("spring boot");

        mockMvc.perform(post("/tags/admin").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+userToken)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access denied. Admins only."));

    }

}
