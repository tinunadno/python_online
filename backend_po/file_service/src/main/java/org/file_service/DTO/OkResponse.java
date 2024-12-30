package org.file_service.DTO;

public class OkResponse {

    String message;
    public OkResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
