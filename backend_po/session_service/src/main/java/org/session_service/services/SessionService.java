package org.session_service.services;

import jakarta.servlet.http.HttpSession;
import org.session_service.DTO.CreateSessionRequest;
import org.session_service.DTO.DeleteSessionRequest;
import org.session_service.entities.SessionEntity;
import org.session_service.repositories.SessionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Map;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionCreationService sessionCreationService;
    private final RequestSendingService requestSendingService;
    //TODO create a batter way to configure addresses
    private final String fileServiceAddress = "http://localhost:8080";

    public SessionService(SessionRepository sessionRepository, SessionCreationService sessionCreationService, RequestSendingService requestSendingService, HttpSession httpSession) {
        this.sessionRepository = sessionRepository;
        this.sessionCreationService = sessionCreationService;
        this.requestSendingService = requestSendingService;
    }

    public Integer JoinSession(String sessionId) throws IllegalArgumentException {
        //session id format is already validated in controller
        SessionEntity sessionEntity = sessionRepository.findSessionById(Integer.parseInt(sessionId));;
        if(sessionEntity == null) {
            throw new IllegalArgumentException("session with this id doesn't exist");
        }

        return sessionEntity.getSessionId();
    }

    public Integer saveSession(CreateSessionRequest createSessionRequest) throws IllegalArgumentException {
        SessionEntity sessionEntity;
        try {
            ResponseEntity<Map> fileServiceResponse = requestSendingService.sendPostRequest(fileServiceAddress+"/fileAPI/createNewFile", null);

            if(fileServiceResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new IllegalArgumentException("file service answered with error");
            }

            if(fileServiceResponse.getBody() == null || fileServiceResponse.getBody().get("fileId") == null) {
                throw new IllegalArgumentException("file service answered with invalid request");
            }

            String sessionFileId = (String) fileServiceResponse.getBody().get("fileId");

            sessionEntity = sessionCreationService.createSessionEntity(createSessionRequest.getUserId(), sessionFileId);
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

    public void removeSession(DeleteSessionRequest deleteSessionRequest) throws IllegalArgumentException {
        //session id format is already validated in DTO
        SessionEntity sessionEntity =  sessionRepository.findSessionBySessionIdAndUserId(Integer.parseInt(deleteSessionRequest.getSessionId()), deleteSessionRequest.getUserId());
        if(sessionEntity == null) {
            throw new IllegalArgumentException("session with this id doesn't exist");
        }
        sessionRepository.delete(sessionEntity);
    }
}
