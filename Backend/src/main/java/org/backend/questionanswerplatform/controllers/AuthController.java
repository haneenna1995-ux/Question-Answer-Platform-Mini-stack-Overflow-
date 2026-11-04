package org.backend.questionanswerplatform.controllers;

import jakarta.validation.Valid;
import org.backend.questionanswerplatform.dto.register.LoginRequest;
import org.backend.questionanswerplatform.dto.register.LoginResponse;
import org.backend.questionanswerplatform.dto.register.RegisterRequest;
import org.backend.questionanswerplatform.dto.register.UserResponse;
import org.backend.questionanswerplatform.logic.AuthService;
import org.backend.questionanswerplatform.logic.TokenBlockListService;
import org.backend.questionanswerplatform.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public final AuthService authService;
    public final TokenBlockListService tokenBlockListService;
    public final JwtService jwtService;

    public AuthController(AuthService authService, TokenBlockListService tokenBlockListService, JwtService jwtService){
        this.authService=authService;
        this.tokenBlockListService=tokenBlockListService;
        this.jwtService=jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request)
    {
        UserResponse response= authService.register(request,"USER");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<UserResponse> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request, "ADMIN"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Instant expiresAt = jwtService.extractExpiration(token);
        tokenBlockListService.revoke(token, expiresAt);
        return ResponseEntity.noContent().build();
    }

}
