package org.web_socket_service.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemporaryFileStorageService {
    private final ConcurrentHashMap<String, String> sessionFiles = new ConcurrentHashMap<>();

    //TODO add file service interaction
    public void addSession(String fileId){
        if(!sessionFiles.containsKey(fileId)){
            sessionFiles.put(fileId, "");
        }
    }

    //TODO add real changes applying!
    public void applyFileChanges(String fileId, String changes){
        if(sessionFiles.containsKey(fileId)) {
            String changedFile = sessionFiles.get(fileId) + changes;
            sessionFiles.put(fileId, changedFile);
        }
    }

    //TODO add file service saving, for till now this will be enough
    public void removeFile(String fileId){
        sessionFiles.remove(fileId);
    }

    public String getSessionFile(String fileId){
        if(sessionFiles.containsKey(fileId)){
            return sessionFiles.get(fileId);
        }
        return "";
    }
}
