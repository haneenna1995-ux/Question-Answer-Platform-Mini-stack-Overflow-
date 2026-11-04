package org.backend.questionanswerplatform.mappers;

import org.backend.questionanswerplatform.dto.register.UserResponse;
import org.backend.questionanswerplatform.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user)
    {
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().getName()

        );
    }
}
