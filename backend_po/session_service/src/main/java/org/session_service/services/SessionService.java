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
    private SessionCreationService sessionCreationService;

    public SessionService(SessionRepository sessionRepository, SessionCreationService sessionCreationService) {
        this.sessionRepository = sessionRepository;
        this.sessionCreationService = sessionCreationService;
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
        SessionEntity sessionEntity;
        try {
            sessionEntity = sessionCreationService.createSessionEntity(createSessionRequest.getUserId(), createSessionRequest.getSessionFileId());
        }catch (RuntimeException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        try{
            SessionEntity SavedSessionEntity = sessionRepository.save(sessionEntity);
            return SavedSessionEntity.getSessionId();
        }catch(DataIntegrityViolationException e){
            throw new IllegalArgumentException("session with this id already exists");
        }
    }
}
