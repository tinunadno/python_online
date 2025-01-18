package org.web_socket_service.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web_socket_service.DTO.ExecutionServiceExecutionRequest;

import java.util.Map;
import java.util.Objects;

@Service
public class ExecutionMicroServiceInteractionService {

    RestTemplate restTemplate = new RestTemplate();
    private final TemporaryFileStorageService temporaryFileStorageService;

    public ExecutionMicroServiceInteractionService(TemporaryFileStorageService temporaryFileStorageService) {
        this.temporaryFileStorageService = temporaryFileStorageService;
    }

    //TODO add 404 handling
    public void sendExecutionRequest(String chatId) throws IllegalArgumentException{
        ExecutionServiceExecutionRequest microServiceRequest = new ExecutionServiceExecutionRequest(
                temporaryFileStorageService.getSessionFile(chatId),
                "http://localhost:8080/webSocketServiceController/callback/" + chatId);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:8081/executionAPI/executeFile",
                microServiceRequest,
                Map.class
        );

        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalArgumentException(Objects.requireNonNull(response.getBody()).toString());
        }
    }
}
