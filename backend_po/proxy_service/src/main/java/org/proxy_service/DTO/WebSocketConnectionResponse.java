package org.proxy_service.DTO;

public class WebSocketConnectionResponse {

    private String webSocketServiceAddress;
    private String sessionId;
    private String token;

    public WebSocketConnectionResponse(String webSocketServiceAddress, String sessionId, String token) {
        this.webSocketServiceAddress = webSocketServiceAddress;
        this.sessionId = sessionId;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
