package org.session_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Random;

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

    public SessionEntity(String userId, String sessionFileId) {
        sessionId = generateRandomSessionId();
        this.userId = userId;
        this.sessionFileId = sessionFileId;
    }

    private Integer generateRandomSessionId() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // генерирует случайное 6-значное число
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
