package org.backend.questionanswerplatform.controllers;

import jakarta.validation.Valid;
import org.backend.questionanswerplatform.dto.question.CreateQuestionRequest;
import org.backend.questionanswerplatform.dto.question.QuestionResponse;
import org.backend.questionanswerplatform.logic.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService)
    {
        this.questionService= questionService;
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(
            @Valid @RequestBody CreateQuestionRequest request,
            @AuthenticationPrincipal String email) {

        QuestionResponse response = questionService.createQuestion(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public  List<QuestionResponse> getAllQuestions()
    {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public QuestionResponse getQuestionById(@PathVariable Long id)
    {
        return questionService.getQuestionById(id);
    }

    @GetMapping("/me")
    public List<QuestionResponse> getMyQuestions(Authentication auth)
    {
        String email=auth.getName();
        return questionService.getMyQuestions(email);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long id,
            @AuthenticationPrincipal String email,
            Authentication authentication) {

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("");

        questionService.deleteQuestion(id, email, role);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public QuestionResponse updateQuestion(@PathVariable Long id,
                                          @Valid @RequestBody CreateQuestionRequest request,
                                           Authentication auth)
    {
        String email =auth.getName();
        String role= auth.getAuthorities().iterator().next().getAuthority();
        return questionService.updateQuestion(id,request,email,role);

    }


}
