package org.session_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JoinSessionRequest {
    @NotBlank(message = "session id required")
    @Size(min = 6, max = 6, message = "session id must be 6 characters long!")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
