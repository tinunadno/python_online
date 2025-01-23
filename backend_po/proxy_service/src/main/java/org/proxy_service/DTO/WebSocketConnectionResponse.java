package org.proxy_service.DTO;

public class WebSocketConnectionResponse {

    private String webSocketServiceAddress;
    private String sessionId;

    public WebSocketConnectionResponse(String address, String sessionId) {
        this.webSocketServiceAddress = address;
        this.sessionId = sessionId;
    }

    public String getWebSocketServiceAddress() {
        return webSocketServiceAddress;
    }

    public void setWebSocketServiceAddress(String webSocketServiceAddress) {
        this.webSocketServiceAddress = webSocketServiceAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
