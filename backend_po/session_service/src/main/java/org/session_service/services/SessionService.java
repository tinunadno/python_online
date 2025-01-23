package org.session_service.services;

import org.session_service.DTO.CreateSessionRequest;
import org.session_service.DTO.DeleteSessionRequest;
import org.session_service.components.ServiceProperties;
import org.session_service.entities.SessionEntity;
import org.session_service.repositories.SessionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionCreationService sessionCreationService;
    private final RequestSendingService requestSendingService;
    private final ServiceProperties serviceProperties;

    public SessionService(SessionRepository sessionRepository, SessionCreationService sessionCreationService, RequestSendingService requestSendingService, ServiceProperties serviceProperties) {
        this.sessionRepository = sessionRepository;
        this.sessionCreationService = sessionCreationService;
        this.requestSendingService = requestSendingService;
        this.serviceProperties = serviceProperties;
    }

    public Integer JoinSession(String sessionId) throws IllegalArgumentException {
        //session id format is already validated in controller
        SessionEntity sessionEntity = sessionRepository.findSessionById(Integer.parseInt(sessionId));
        if(sessionEntity == null) {
            throw new IllegalArgumentException("session with this id doesn't exist");
        }

        return sessionEntity.getSessionId();
    }

    public String getSessionFileId(String sessionId) throws IllegalArgumentException {
        //session id format is already validated in controller
        SessionEntity sessionEntity = sessionRepository.findSessionById(Integer.parseInt(sessionId));
        if(sessionEntity == null) {
            throw new IllegalArgumentException("session with this id doesn't exist");
        }

        return sessionEntity.getSessionFileId();
    }

    public Integer saveSession(CreateSessionRequest createSessionRequest) throws IllegalArgumentException {
        SessionEntity sessionEntity;
        try {
            ResponseEntity<Map> fileServiceResponse = requestSendingService.sendPostRequest(serviceProperties.getFileServiceName(), serviceProperties.getCreateFileEndpoint(), null);

            if(fileServiceResponse.getStatusCode() == HttpStatus.BAD_REQUEST || fileServiceResponse.getStatusCode() != HttpStatus.OK){
                throw new IllegalArgumentException((fileServiceResponse.getBody().get("message") == null ? "invalid response from file service" : "file service answered with error: "+fileServiceResponse.getBody().get("message").toString()));
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

        String sessionFileId = sessionEntity.getSessionFileId();
        Map<String, String> fileDeleteRequest = new HashMap<>();
        fileDeleteRequest.put("fileId", sessionFileId);

        ResponseEntity<Map> response = requestSendingService.sendPostRequestAsMap(serviceProperties.getFileServiceName(),serviceProperties.getDeleteFileEndpoint(), fileDeleteRequest);
        if(response.getStatusCode() == HttpStatus.BAD_REQUEST || response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException((response.getBody().get("message") == null ? "invalid response from file service" : "file service answered with error: "+response.getBody().get("message").toString()));
        }

        sessionRepository.delete(sessionEntity);
    }
}
