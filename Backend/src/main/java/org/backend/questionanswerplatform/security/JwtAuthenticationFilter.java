package org.backend.questionanswerplatform.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.backend.questionanswerplatform.logic.TokenBlockListService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 public final JwtService jwtService;
 public final TokenBlockListService tokenBlockListService;

    public JwtAuthenticationFilter(JwtService jwtService, TokenBlockListService tokenBlockListService) {
        this.jwtService = jwtService;
        this.tokenBlockListService=tokenBlockListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException
    {
        String header= request.getHeader("Authorization");
        if(header!= null && header.startsWith("Bearer "))
        {
            String token=header.substring(7);

            if(jwtService.isValid(token) &&!tokenBlockListService.isRevoked(token))
            {
                String email= jwtService.extractEmail(token);
                String role= jwtService.extractRole(token);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,// principle
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))// authorities - every user has one role
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }
        chain.doFilter(request, response);

    }
}
