package org.user_db_service.DTO;


import jakarta.validation.constraints.*;

public class UserLoginRequest {
    private String username;
    @Email(message = "email must be valid", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password length must be at least 8 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @AssertTrue(message = "email or username required")
    public boolean isEmailOrUsernameExists(){
        return this.email != null && !this.email.isEmpty() || this.username != null && !this.username.isEmpty();
    }

}
