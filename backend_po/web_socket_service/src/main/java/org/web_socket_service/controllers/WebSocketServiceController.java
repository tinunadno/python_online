package org.web_socket_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web_socket_service.DTO.DeleteSessionRequest;
import org.web_socket_service.DTO.FileContentResponse;
import org.web_socket_service.DTO.otherServicesDTO.ExecutionServiceResponse;
import org.web_socket_service.DTO.WebSocketResponse;
import org.web_socket_service.services.ActiveSessionTracker;
import org.web_socket_service.services.ExecutionMicroServiceInteractionService;
import org.web_socket_service.services.TemporaryFileStorageService;
import org.web_socket_service.services.TopicMessageSender;

@RestController
@RequestMapping("/webSocketServiceController")
public class WebSocketServiceController {

    private final TemporaryFileStorageService temporaryFileStorageService;
    private final ExecutionMicroServiceInteractionService executionMicroServiceInteractionService;
    private final TopicMessageSender topicMessageSender;

    public WebSocketServiceController(ExecutionMicroServiceInteractionService executionMicroServiceInteractionService, TemporaryFileStorageService temporaryFileStorageService, TopicMessageSender topicMessageSender) {
        this.executionMicroServiceInteractionService = executionMicroServiceInteractionService;
        this.temporaryFileStorageService = temporaryFileStorageService;
        this.topicMessageSender = topicMessageSender;
    }

    @PostMapping("/executeFile/{sessionId}")
    public ResponseEntity<?> execute(@PathVariable String sessionId) {
        try {
            executionMicroServiceInteractionService.sendExecutionRequest(sessionId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/callback/{sessionId}")
    public void callback(@PathVariable String sessionId, @RequestBody ExecutionServiceResponse executionServiceResponse) {
        topicMessageSender.sendMessage(new WebSocketResponse(WebSocketResponse.MessageType.EXECUTION_RESULT, executionServiceResponse.getExecutionOutput()), sessionId);
    }

    @GetMapping("/getFileContent/{sessionId}")
    public ResponseEntity<?> getFileContent(@PathVariable String sessionId) {
        String content = temporaryFileStorageService.getSessionFile(sessionId);
        FileContentResponse fileContentResponse = new FileContentResponse(content);
        return new ResponseEntity<>(fileContentResponse, HttpStatus.OK);
    }

    @PostMapping("/removeSessionById")
    public ResponseEntity<?> removeSessionById(@RequestBody DeleteSessionRequest deleteSessionRequest) {
        topicMessageSender.disconnectSession(deleteSessionRequest.getSessionId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
