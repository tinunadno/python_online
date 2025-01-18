package org.web_socket_service.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TopicMessageSender {

    private final SimpMessagingTemplate messagingTemplate;

    public TopicMessageSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(Object responseBody, String sessionId){
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, responseBody);
    }
}
