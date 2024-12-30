package org.session_service.DTO;

public class OkResponse {
    private String sessionId;

    public OkResponse(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
