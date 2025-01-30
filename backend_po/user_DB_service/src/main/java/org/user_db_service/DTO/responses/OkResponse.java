package org.user_db_service.DTO.responses;

public class OkResponse {

    private String userId;

    public OkResponse(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
