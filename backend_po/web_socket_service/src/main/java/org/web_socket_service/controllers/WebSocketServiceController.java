package org.web_socket_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.web_socket_service.DTO.ExecutionServiceResponse;
import org.web_socket_service.services.ExecutionMicroServiceInteractionService;
import org.web_socket_service.services.TemporaryFileStorageService;

@RestController
@RequestMapping("/webSocketServiceController")
public class WebSocketServiceController {

    private final TemporaryFileStorageService temporaryFileStorageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutionMicroServiceInteractionService executionMicroServiceInteractionService;

    public WebSocketServiceController(SimpMessagingTemplate messagingTemplate, ExecutionMicroServiceInteractionService executionMicroServiceInteractionService, TemporaryFileStorageService temporaryFileStorageService) {
        this.messagingTemplate = messagingTemplate;
        this.executionMicroServiceInteractionService = executionMicroServiceInteractionService;
        this.temporaryFileStorageService = temporaryFileStorageService;
    }

    @PostMapping("/executeFile/{chatId}")
    public ResponseEntity<String> execute(@PathVariable String chatId) {
        try {
            executionMicroServiceInteractionService.sendExecutionRequest(chatId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/callback/{chatId}")
    public void callback(@PathVariable String chatId, @RequestBody ExecutionServiceResponse executionServiceResponse) {
        //TODO add normal message for users
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, "execution result: " + executionServiceResponse.getExecutionOutput());
    }

    @GetMapping("/getFileContent/{chatId}")
    public ResponseEntity<String> getFileContent(@PathVariable String chatId) {
        return new ResponseEntity<>(temporaryFileStorageService.getSessionFile(chatId), HttpStatus.OK);
    }
}
