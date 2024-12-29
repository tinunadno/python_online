package org.session_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeleteSessionRequest {
    @NotBlank(message = "user id required")
    private String userId;
    @NotBlank(message = "session id required")
    @Size(min = 6, max = 6, message = "session id must be 6 characters long")
    @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digit integer")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
