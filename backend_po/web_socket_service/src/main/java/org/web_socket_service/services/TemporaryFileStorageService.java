package org.web_socket_service.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.web_socket_service.DTO.otherServicesDTO.FileServiceUpdateFileContentRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemporaryFileStorageService {
    private final MicroServiceRestInteractionService microServiceRestInteractionService;
    private final ConcurrentHashMap<String, SessionFileInstance> sessionFiles = new ConcurrentHashMap<>();

    public TemporaryFileStorageService(MicroServiceRestInteractionService microServiceRestInteractionService) {
        this.microServiceRestInteractionService = microServiceRestInteractionService;
    }

    public void addSession(String sessionId){
        if(sessionFiles.containsKey(sessionId)){
            return;
        }
        //TODO do something with microservices url's

        ResponseEntity<Map> response;
        try {
            response = microServiceRestInteractionService.sendGetRequest("http://localhost:8082/sessionAPI/getFileDescriptor?sessionId=" + sessionId);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send request to session service");
        }
        String fileDescriptor;
        try {
            fileDescriptor = response.getBody().get("sessionId").toString();
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "session service responsed with invalid file");
        }

        ResponseEntity<Map> fileServiceResponse;
        try {
            fileServiceResponse = microServiceRestInteractionService.sendGetRequest("http://localhost:8083/fileAPI/getFileContent?fileId=" + fileDescriptor);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if(fileServiceResponse.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send request to file service");
        }
        String fileContent;
        try {
            fileContent = fileServiceResponse.getBody().get("content").toString();
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "file service responsed with invalid file");
        }

        if(!sessionFiles.containsKey(sessionId)){
            sessionFiles.put(sessionId, new SessionFileInstance(fileDescriptor, fileContent));
        }
    }

    //TODO add real changes applying!
    public void applyFileChanges(String fileId, String changes){
        if(sessionFiles.containsKey(fileId)) {
            SessionFileInstance changedFile = sessionFiles.get(fileId);
            changedFile.setFileContent(changedFile.getFileContent() + changes);
            sessionFiles.put(fileId, changedFile);
        }
    }

    public void closeTemporarySessionStorage(String fileId){

        String fileDescriptor = sessionFiles.get(fileId).getFileDescriptor();
        String fileContent = sessionFiles.get(fileId).getFileContent();

        FileServiceUpdateFileContentRequest request = new FileServiceUpdateFileContentRequest(fileDescriptor, fileContent);

        microServiceRestInteractionService.sendPostRequest("http://localhost:8083/fileAPI/updateFileContent", request);

        sessionFiles.remove(fileId);
    }

    public String getSessionFile(String fileId){
        if(sessionFiles.containsKey(fileId)){
            return sessionFiles.get(fileId).getFileContent();
        }
        return "";
    }

    private static class SessionFileInstance{
        private final String fileDescriptor;

        private String fileContent;

        public SessionFileInstance(String fileDescriptor, String fileContent){
            this.fileDescriptor = fileDescriptor;
            this.fileContent = fileContent;
        }

        public String getFileContent() {
            return fileContent;
        }

        public void setFileContent(String fileContent) {
            this.fileContent = fileContent;
        }

        public String getFileDescriptor() {
            return fileDescriptor;
        }
    }
}
