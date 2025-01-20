package org.proxy_service.DTO;

import jakarta.validation.constraints.NotBlank;

public class CreateSessionRequest {

    @NotBlank(message = "user id is required")
    private String userId;

    public CreateSessionRequest(String userId) {
        this.userId = userId;
    }

    public @NotBlank(message = "user id is required") String getUserId() {
        return userId;
    }

    public void setUserId(@NotBlank(message = "user id is required") String userId) {
        this.userId = userId;
    }
}
