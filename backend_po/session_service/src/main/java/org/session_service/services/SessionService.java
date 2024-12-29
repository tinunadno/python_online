package org.session_service.services;

import org.session_service.DTO.CreateSessionRequest;
import org.session_service.DTO.JoinSessionRequest;
import org.session_service.entities.SessionEntity;
import org.session_service.repositories.SessionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Integer JoinSession(JoinSessionRequest joinSessionRequest) throws IllegalArgumentException {
        SessionEntity sessionEntity;
        try {
            sessionEntity = sessionRepository.findSessionById(Integer.parseInt(joinSessionRequest.getSessionId()));
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid session id");
        }
        if(sessionEntity == null) {
            throw new IllegalArgumentException("session with this id doesn't exist");
        }

        return sessionEntity.getSessionId();
    }

    public Integer saveSession(CreateSessionRequest createSessionRequest) throws IllegalArgumentException {
        SessionEntity sessionEntity = new SessionEntity(createSessionRequest.getUserId(), createSessionRequest.getSessionFileId());
        try{
            SessionEntity SavedSessionEntity = sessionRepository.save(sessionEntity);
            return SavedSessionEntity.getSessionId();
        }catch(DataIntegrityViolationException e){
            throw new IllegalArgumentException("session with this id already exists");
        }
    }
}
