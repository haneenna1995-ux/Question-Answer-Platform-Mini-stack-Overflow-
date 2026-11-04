package org.backend.questionanswerplatform.controllers;

import jakarta.validation.Valid;
import org.backend.questionanswerplatform.dto.answer.AnswerResponse;
import org.backend.questionanswerplatform.dto.answer.CreateAnswerRequest;
import org.backend.questionanswerplatform.logic.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController (AnswerService answerService)
    {
        this.answerService=answerService;

    }

    @GetMapping
    public List<AnswerResponse> getAnswers(@PathVariable Long questionId){

        return answerService.getAnswersForQuestion(questionId);
    }

    @PostMapping
    public ResponseEntity<AnswerResponse> createAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody CreateAnswerRequest request,
            @AuthenticationPrincipal String email) {

        AnswerResponse response = answerService.createAnswer(questionId, request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
