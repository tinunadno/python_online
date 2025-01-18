package org.web_socket_service.components;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.web_socket_service.services.ActiveUserTracker;
import org.web_socket_service.services.TemporaryFileStorageService;

@Component
public class WebSocketEventListener {

    private final ActiveUserTracker activeUserTracker;
    private final TemporaryFileStorageService temporaryFileStorageService;

    public WebSocketEventListener(ActiveUserTracker activeUserTracker, TemporaryFileStorageService temporaryFileStorageService) {
        this.activeUserTracker = activeUserTracker;
        this.temporaryFileStorageService = temporaryFileStorageService;
    }

        @EventListener
        public void handleWebSocketConnectListener(SessionConnectEvent event) {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
            String humanReadableSessionId = accessor.getFirstNativeHeader("sessionId");
            String webSocketSessionId = accessor.getSessionId();
            if(humanReadableSessionId!=null && webSocketSessionId!=null) {
                activeUserTracker.addConnection( webSocketSessionId, humanReadableSessionId);
                temporaryFileStorageService.addSession(humanReadableSessionId);
            }

        }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String webSocketSessionId = accessor.getSessionId();
        if(activeUserTracker.decrementConnection(webSocketSessionId) == 0){
            String humanReadableSessionId = activeUserTracker.getHumanReadableSessionId(webSocketSessionId);
            activeUserTracker.removeConnection(webSocketSessionId);
            temporaryFileStorageService.closeTemporarySessionStorage(humanReadableSessionId);
        }
    }

}
