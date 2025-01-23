package org.web_socket_service.DTO.otherServicesDTO;

public class GetFileDescriptorRequest {

    private String sessionId;

    public GetFileDescriptorRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
