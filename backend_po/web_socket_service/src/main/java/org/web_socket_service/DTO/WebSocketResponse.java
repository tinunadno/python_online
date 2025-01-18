package org.web_socket_service.DTO;

public class WebSocketResponse {

    private MessageType messageType;

    private String message;

    public enum MessageType{
        CHANGES,
        EXECUTION_RESULT,
        ERROR
    }

    public WebSocketResponse(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
