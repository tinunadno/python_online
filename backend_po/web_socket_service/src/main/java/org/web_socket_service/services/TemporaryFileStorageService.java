package org.web_socket_service.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.web_socket_service.DTO.otherServicesDTO.FileServiceUpdateFileContentRequest;
import org.web_socket_service.DTO.otherServicesDTO.GetFileContentRequest;
import org.web_socket_service.DTO.otherServicesDTO.GetFileDescriptorRequest;
import org.web_socket_service.components.ServiceProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemporaryFileStorageService {

    private final MicroServiceRestInteractionService microServiceRestInteractionService;
    private final ConcurrentHashMap<String, SessionFileInstance> sessionFiles = new ConcurrentHashMap<>();
    private final ServiceProperties serviceProperties;

    public TemporaryFileStorageService(MicroServiceRestInteractionService microServiceRestInteractionService, ServiceProperties serviceProperties) {
        this.microServiceRestInteractionService = microServiceRestInteractionService;
        this.serviceProperties = serviceProperties;
    }

    public boolean fileExists(String sessionId){
        return sessionFiles.containsKey(sessionId);
    }

    public void addSession(String sessionId){
        if(sessionFiles.containsKey(sessionId)){
            return;
        }

        //it's necessary, if file service is not available
        sessionFiles.put(sessionId, new SessionFileInstance("", ""));

        ResponseEntity<Map> response;
        try {
            response = microServiceRestInteractionService.sendGetRequest(serviceProperties.getSessionServiceName(),serviceProperties.getGetSessionFileDescriptorEndpoint() , new GetFileDescriptorRequest(sessionId));
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
            fileServiceResponse = microServiceRestInteractionService.sendGetRequest(serviceProperties.getFileServiceName(), serviceProperties.getGetFileContentEndpoint(), new GetFileContentRequest(fileDescriptor));
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

        sessionFiles.put(sessionId, new SessionFileInstance(fileDescriptor, fileContent));
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
        if(!fileDescriptor.isEmpty()) {
            String fileContent = sessionFiles.get(fileId).getFileContent();

            FileServiceUpdateFileContentRequest request = new FileServiceUpdateFileContentRequest(fileDescriptor, fileContent);

            microServiceRestInteractionService.sendPostRequest(serviceProperties.getFileServiceName(), serviceProperties.getUpdateFileContentEndpoint(), request);
        }
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
