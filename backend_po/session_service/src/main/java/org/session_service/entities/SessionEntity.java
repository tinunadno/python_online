package org.session_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "sessions")
public class SessionEntity {
    @Id
    @Column(name = "session_id", nullable = false, unique = true)
    private Integer sessionId;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "session_file_id", nullable = false)
    private String sessionFileId;

    public SessionEntity() {}

    public SessionEntity(Integer sessionId, String userId, String sessionFileId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.sessionFileId = sessionFileId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionFileId() {
        return sessionFileId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionFileId(String sessionFileId) {
        this.sessionFileId = sessionFileId;
    }
}
