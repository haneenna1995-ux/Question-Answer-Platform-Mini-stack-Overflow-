package org.backend.questionanswerplatform.dto.register;

public class LoginResponse {
    private String token;
    private UserResponse user;

    public LoginResponse(){
        //needed because I added constructor with parameters
    }
    public LoginResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserResponse getUser() {
        return user;
    }

}
