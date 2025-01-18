package org.web_socket_service.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ExecuteRequest {

    @NotNull(message = "session id is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digits integer")
    private String sessionId;

    public ExecuteRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
