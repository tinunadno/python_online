package org.web_socket_service.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.web_socket_service.services.TemporaryFileStorageService;

@Controller
public class ChatController {

    private final TemporaryFileStorageService temporaryFileStorageService;

    public ChatController(TemporaryFileStorageService temporaryFileStorageService) {
        this.temporaryFileStorageService = temporaryFileStorageService;
    }

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public String sendMessage(@DestinationVariable String chatId, String changes) {
        temporaryFileStorageService.applyFileChanges(chatId, changes);
        return "Чат " + chatId + ": " + changes;
    }
}
