package org.user_db_service.DTO;


import jakarta.validation.constraints.*;

public class UserLoginRequest {
    @NotBlank(message = "user name is required")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password length must be at least 8 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
