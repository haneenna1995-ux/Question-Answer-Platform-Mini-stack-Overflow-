CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(20) NOT NULL,
                       PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6) DEFAULT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       user_name VARCHAR(50) NOT NULL,
                       role_id BIGINT NOT NULL,
                       failed_login_attempts INT NOT NULL,
                       lockout_until DATETIME(6) DEFAULT NULL,
                       PRIMARY KEY (id),
                       UNIQUE KEY UK6dotkott2kjsp8vw4d0m25fb7 (email),
                       CONSTRAINT FKp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES roles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tag (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     name VARCHAR(255) DEFAULT NULL,
                     PRIMARY KEY (id),
                     UNIQUE KEY UK1wdpsed5kna2y38hnbgrnhi5b (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE question (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          created_at DATETIME(6) DEFAULT NULL,
                          description TEXT,
                          title VARCHAR(255) DEFAULT NULL,
                          user_id BIGINT NOT NULL,
                          PRIMARY KEY (id),
                          CONSTRAINT FK7rnpup7eaonh2ubt922ormoij FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE question_tag (
                              question_id BIGINT NOT NULL,
                              tag_id BIGINT NOT NULL,
                              PRIMARY KEY (question_id, tag_id),
                              CONSTRAINT FK44ydihbi2qk8k96175quf5q63 FOREIGN KEY (question_id) REFERENCES question (id),
                              CONSTRAINT FKnacet7y1n8llxvrbmm3xdq13j FOREIGN KEY (tag_id) REFERENCES tag (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE answer (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        body TEXT NOT NULL,
                        created_at DATETIME(6) DEFAULT NULL,
                        question_id BIGINT NOT NULL,
                        user_id BIGINT NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT FK8frr4bcabmmeyyu60qt7iiblo FOREIGN KEY (question_id) REFERENCES question (id),
                        CONSTRAINT FKsdj8jab9t00diflkysw22k7bv FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE revoked_tokens (
                                token varchar(512) NOT NULL,
                                expires_at datetime(6) NOT NULL,
                                PRIMARY KEY (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci