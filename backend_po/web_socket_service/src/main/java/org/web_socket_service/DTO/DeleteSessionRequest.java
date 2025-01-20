package org.web_socket_service.DTO;

import jakarta.validation.constraints.NotNull;

public class DeleteSessionRequest {

    @NotNull(message = "session id is required")
    private String sessionId;

    public DeleteSessionRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public @NotNull(message = "session id is required") String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@NotNull(message = "session id is required") String sessionId) {
        this.sessionId = sessionId;
    }
}
