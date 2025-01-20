package org.web_socket_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.web_socket_service.DTO.WebSocketResponse;
import org.web_socket_service.services.TemporaryFileStorageService;

@Controller
public class SessionController {

    private final TemporaryFileStorageService temporaryFileStorageService;

    public SessionController(TemporaryFileStorageService temporaryFileStorageService) {
        this.temporaryFileStorageService = temporaryFileStorageService;
    }

    @MessageMapping("/session/{sessionId}")
    @SendTo("/topic/session/{sessionId}")
    public ResponseEntity<?> sendMessage(@DestinationVariable String sessionId, String changes) {
        temporaryFileStorageService.applyFileChanges(sessionId, changes);
        return new ResponseEntity<>(new WebSocketResponse(WebSocketResponse.MessageType.CHANGES, changes), HttpStatus.OK);
    }
}
