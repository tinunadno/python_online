package org.web_socket_service.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.web_socket_service.DTO.WebSocketResponse;
import org.web_socket_service.DTO.otherServicesDTO.ExecutionServiceExecutionRequest;
import org.web_socket_service.components.ServiceProperties;

import java.util.Map;
import java.util.Objects;

@Service
public class ExecutionMicroServiceInteractionService {

    private final MicroServiceRestInteractionService microServiceRestInteractionService;
    private final TemporaryFileStorageService temporaryFileStorageService;
    private final TopicMessageSender topicMessageSender;
    private final ServiceProperties serviceProperties;
    private final JWTService jwtService;

    public ExecutionMicroServiceInteractionService(TemporaryFileStorageService temporaryFileStorageService, TopicMessageSender topicMessageSender, MicroServiceRestInteractionService microServiceRestInteractionService, ServiceProperties serviceProperties, JWTService jwtService) {
        this.temporaryFileStorageService = temporaryFileStorageService;
        this.topicMessageSender = topicMessageSender;
        this.microServiceRestInteractionService = microServiceRestInteractionService;
        this.serviceProperties = serviceProperties;
        this.jwtService = jwtService;
    }

    //i'll just put it here :D
    public String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public void sendExecutionRequest(String sessionId) throws IllegalArgumentException {

        //this kinda weird, but its the easiest way to add tokens to callback
        ExecutionServiceExecutionRequest microServiceRequest = new ExecutionServiceExecutionRequest(
                temporaryFileStorageService.getSessionFile(sessionId),
                getBaseUrl()+"/webSocketServiceController/callback/" + sessionId,
                           jwtService.generateToken("WEB_SOCKET_SERVICE", "callBackToken"));

        ResponseEntity<Map> response;
        try {
            //TODO do something with microservices url's
            response = microServiceRestInteractionService.sendPostRequest(serviceProperties.getExecutionServiceName(), serviceProperties.getExecuteFileEndpoint(), microServiceRequest);
        } catch (Exception e) {
            topicMessageSender.sendMessage(
                    new WebSocketResponse(
                            WebSocketResponse.MessageType.ERROR,
                            "file execution service is not available now"
                    ),
                    sessionId
            );
            return;
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(Objects.requireNonNull(response.getBody()).toString());
        }
    }
}
