package org.proxy_service.DTO;

public class WebSocketConnectionResponse {

    private String address;
    private String sessionId;

    public WebSocketConnectionResponse(String address, String sessionId) {
        this.address = address;
        this.sessionId = sessionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
