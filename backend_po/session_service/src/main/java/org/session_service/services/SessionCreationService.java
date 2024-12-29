package org.session_service.services;

import org.session_service.entities.SessionEntity;
import org.session_service.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class SessionCreationService {

    private final SessionRepository sessionRepository;

    public SessionCreationService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionEntity createSessionEntity(String userId, String fileId){
        Integer sessionId = generateUniqueSessionId(fileId);
        return new SessionEntity(sessionId, userId, fileId);
    }

    private int generateUniqueSessionId(String fileId) {
        int total_attempts = 0;
        int sessionId;
        int max_attempts = 5;
        do {
            sessionId = generateHashId(fileId);
            total_attempts++;
        } while (sessionRepository.findSessionById(sessionId) != null && total_attempts < max_attempts);
        if(total_attempts == max_attempts){
            throw new RuntimeException("Session creation failed");
        }
        return sessionId;
    }

    private int generateHashId(String fileId) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(fileId.getBytes());
            BigInteger hashValue = new BigInteger(1, hashBytes);
            return (hashValue.mod(BigInteger.valueOf(900_000)).add(BigInteger.valueOf(100_000)).intValue());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("can't find SHA-256 algorithm", e);
        }
    }
}
