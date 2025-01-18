package org.web_socket_service.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.web_socket_service.DTO.WebSocketResponse;
import org.web_socket_service.DTO.otherServicesDTO.ExecutionServiceExecutionRequest;

import java.util.Map;
import java.util.Objects;

@Service
public class ExecutionMicroServiceInteractionService {

    RestTemplate restTemplate = new RestTemplate();
    private final TemporaryFileStorageService temporaryFileStorageService;
    private final TopicMessageSender topicMessageSender;

    public ExecutionMicroServiceInteractionService(TemporaryFileStorageService temporaryFileStorageService, TopicMessageSender topicMessageSender) {
        this.temporaryFileStorageService = temporaryFileStorageService;
        this.topicMessageSender = topicMessageSender;
    }

    //TODO add 404 handling
    public void sendExecutionRequest(String sessionId) throws IllegalArgumentException {
        ExecutionServiceExecutionRequest microServiceRequest = new ExecutionServiceExecutionRequest(
                temporaryFileStorageService.getSessionFile(sessionId),
                "http://localhost:8080/webSocketServiceController/callback/" + sessionId);
        ResponseEntity<Map> response;
        try {
            response = restTemplate.postForEntity(
                    "http://localhost:8081/executionAPI/executeFile",
                    microServiceRequest,
                    Map.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            topicMessageSender.sendMessage(
                    new WebSocketResponse(
                            WebSocketResponse.MessageType.ERROR,
                            "file execution service is not available now"
                    ),
                    sessionId
            );
            return;
        } catch (Exception e) {
            topicMessageSender.sendMessage(
                    new WebSocketResponse(WebSocketResponse.MessageType.ERROR, e.getMessage()),
                    sessionId
            );
            return;
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(Objects.requireNonNull(response.getBody()).toString());
        }
    }
}
