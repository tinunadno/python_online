package org.session_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSessionRequest {
    @NotBlank(message = "user id is required")
    private String userId;
    @NotBlank(message = "session file id is required")
    @Size(min = 24, max = 24, message = "file id length must be 24 characters")
    private String fileId;

    public String getUserId() {
        return userId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
