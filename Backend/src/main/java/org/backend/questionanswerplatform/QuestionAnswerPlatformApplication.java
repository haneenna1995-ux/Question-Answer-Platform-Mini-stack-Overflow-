package org.backend.questionanswerplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuestionAnswerPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionAnswerPlatformApplication.class, args);
    }

}
