package org.web_socket_service.components;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.web_socket_service.DTO.WebSocketResponse;
import org.web_socket_service.services.ActiveSessionTracker;
import org.web_socket_service.services.TemporaryFileStorageService;
import org.web_socket_service.services.TopicMessageSender;

@Component
public class WebSocketEventListener {

    private final ActiveSessionTracker activeSessionTracker;
    private final TemporaryFileStorageService temporaryFileStorageService;
    private final TopicMessageSender topicMessageSender;

    public WebSocketEventListener(ActiveSessionTracker activeSessionTracker, TemporaryFileStorageService temporaryFileStorageService, TopicMessageSender topicMessageSender) {
        this.activeSessionTracker = activeSessionTracker;
        this.temporaryFileStorageService = temporaryFileStorageService;
        this.topicMessageSender= topicMessageSender;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String humanReadableSessionId = accessor.getFirstNativeHeader("sessionId");
        String webSocketSessionId = accessor.getSessionId();
        if (humanReadableSessionId != null && webSocketSessionId != null ) {
            activeSessionTracker.addConnection(webSocketSessionId, humanReadableSessionId);
            if(!temporaryFileStorageService.fileExists(humanReadableSessionId)) {
                try {
                    temporaryFileStorageService.addSession(humanReadableSessionId);
                } catch (ResponseStatusException e) {
                    topicMessageSender.sendMessage(new WebSocketResponse(
                            WebSocketResponse.MessageType.ERROR, e.getMessage()
                    ), humanReadableSessionId);
                }
            }
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String webSocketSessionId = accessor.getSessionId();
        if (activeSessionTracker.decrementConnection(webSocketSessionId) <= 0) {
            String humanReadableSessionId = activeSessionTracker.getHumanReadableSessionId(webSocketSessionId);
            activeSessionTracker.removeConnection(webSocketSessionId);
            temporaryFileStorageService.closeTemporarySessionStorage(humanReadableSessionId);
        }
    }

}
