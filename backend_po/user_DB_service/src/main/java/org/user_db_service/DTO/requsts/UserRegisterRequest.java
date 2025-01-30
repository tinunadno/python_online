package org.user_db_service.DTO.requsts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterRequest {
    @NotBlank(message = "user name is required")
    private String username;
    @NotBlank(message = "email is required")
    @Email(message = "e", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password length must be at least 8 characters")
    private String password;

    public @NotBlank(message = "user name is required") String getUsername() {
        return username;
    }

    public @NotBlank(message = "email is required") @Email(message = "email must be valid", flags = {Pattern.Flag.CASE_INSENSITIVE}) String getEmail() {
        return email;
    }

    public @NotBlank(message = "Password is required") @Size(min = 8, message = "Password length must be at least 8 characters") String getPassword() {
        return password;
    }

    public void setUsername(@NotBlank(message = "user name is required") String username) {
        this.username = username;
    }

    public void setEmail(@NotBlank(message = "email is required") @Email(message = "email must be valid", flags = {Pattern.Flag.CASE_INSENSITIVE}) String email) {
        this.email = email;
    }

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 8, message = "Password length must be at least 8 characters") String password) {
        this.password = password;
    }
}
