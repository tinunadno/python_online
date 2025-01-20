package org.proxy_service.DTO;

import jakarta.validation.constraints.NotNull;

public class CloseWebSocketConnectionRequest {

    @NotNull(message = "session id is required")
    private String sessionId;

    public CloseWebSocketConnectionRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public @NotNull(message = "session id is required") String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@NotNull(message = "session id is required") String sessionId) {
        this.sessionId = sessionId;
    }
}
