package org.backend.questionanswerplatform.config;

import jakarta.servlet.http.HttpServletResponse;
import org.backend.questionanswerplatform.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String ROLE_ADMIN="ADMIN";
    private static final String ROLE_USER="USER";
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

   @Bean
    public PasswordEncoder passwordEncoder (){
     return new BCryptPasswordEncoder();
 }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication required. Please log in.\"}");
        };
    }


    public class SecurityConfigurationException extends RuntimeException {
        public SecurityConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http
                    
                    .cors(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(sm -> sm
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(eh -> eh
                            .authenticationEntryPoint(authenticationEntryPoint())
                            .accessDeniedHandler((request, response, ex) -> {
                                response.setContentType("application/json");
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("{\"error\":\"Access denied. Admins only.\"}");
                            })
                    )

                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/ws/**").permitAll()
                            .requestMatchers("/auth/login").permitAll()
                            .requestMatchers("/auth/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth/admin").hasRole(ROLE_ADMIN)
                            .requestMatchers(HttpMethod.GET, "/questions").permitAll()
                            .requestMatchers(HttpMethod.GET, "/questions/{id}/answers").permitAll()
                            .requestMatchers(HttpMethod.GET, "/questions/{id}").permitAll()
                            .requestMatchers(HttpMethod.POST,"/tags/admin").hasRole(ROLE_ADMIN)
                            .requestMatchers("/tags/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                            .requestMatchers(HttpMethod.POST, "/questions").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                            .requestMatchers(HttpMethod.POST, "/questions/{id}/answers").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/questions/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/questions/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                            .requestMatchers(HttpMethod.GET, "/questions/me").authenticated()
                            .anyRequest().authenticated())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);// registers the filter and apply the filter before the build-in filter

            return http.build();

        } catch (Exception e) {
            throw new SecurityConfigurationException("Failed to configure security filter chain", e);
        }
    }


}
