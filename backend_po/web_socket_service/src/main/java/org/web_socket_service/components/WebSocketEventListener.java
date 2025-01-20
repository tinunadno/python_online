package org.web_socket_service.components;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.web_socket_service.services.ActiveSessionTracker;
import org.web_socket_service.services.TemporaryFileStorageService;

@Component
public class WebSocketEventListener {

    private final ActiveSessionTracker activeSessionTracker;
    private final TemporaryFileStorageService temporaryFileStorageService;

    public WebSocketEventListener(ActiveSessionTracker activeSessionTracker, TemporaryFileStorageService temporaryFileStorageService) {
        this.activeSessionTracker = activeSessionTracker;
        this.temporaryFileStorageService = temporaryFileStorageService;
    }

        @EventListener
        public void handleWebSocketConnectListener(SessionConnectEvent event) {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
            String humanReadableSessionId = accessor.getFirstNativeHeader("sessionId");
            String webSocketSessionId = accessor.getSessionId();
            if(humanReadableSessionId!=null && webSocketSessionId!=null) {
                WebSocketSession session = (WebSocketSession) accessor.getSessionAttributes().get("webSocketSession");
                activeSessionTracker.addConnection( webSocketSessionId, humanReadableSessionId, session);
                temporaryFileStorageService.addSession(humanReadableSessionId);
            }

        }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String webSocketSessionId = accessor.getSessionId();
        if(activeSessionTracker.decrementConnection(webSocketSessionId) <= 0){
            String humanReadableSessionId = activeSessionTracker.getHumanReadableSessionId(webSocketSessionId);
            activeSessionTracker.removeConnection(webSocketSessionId);
            temporaryFileStorageService.closeTemporarySessionStorage(humanReadableSessionId);
        }
    }

}
