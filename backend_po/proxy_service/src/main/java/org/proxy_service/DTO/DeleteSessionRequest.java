package org.proxy_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DeleteSessionRequest {

    @NotBlank(message = "session id is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digit integer")
    private String sessionId;

    @NotBlank(message = "user id is required")
    private String userId;

    public DeleteSessionRequest(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public @NotBlank(message = "session id is required") @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digit integer") String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@NotBlank(message = "session id is required") @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digit integer") String sessionId) {
        this.sessionId = sessionId;
    }

    public @NotBlank(message = "user id is required") String getUserId() {
        return userId;
    }

    public void setUserId(@NotBlank(message = "user id is required") String userId) {
        this.userId = userId;
    }

}
