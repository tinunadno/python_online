package org.proxy_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SessionConnectionRequest {

    @NotBlank(message = "session id is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digit integer")
    private String sessionId;

    @NotBlank(message = "user id is required")
    private String userId;

    public SessionConnectionRequest(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
