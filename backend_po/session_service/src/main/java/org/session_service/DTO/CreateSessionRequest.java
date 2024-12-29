package org.session_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSessionRequest {
    @NotBlank(message = "user id is required")
    private String userId;
    @NotBlank(message = "session file id is required")
    @Size(min = 24, max = 24, message = "file id length must be 24 characters")
    private String sessionFileId;

    public String getUserId() {
        return userId;
    }

    public String getSessionFileId() {
        return sessionFileId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionFileId(String sessionFileId) {
        this.sessionFileId = sessionFileId;
    }
}
