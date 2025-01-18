package org.web_socket_service.components;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(ActiveUserTracker activeUserTracker, TemporaryFileStorageService temporaryFileStorageService, SimpMessagingTemplate messagingTemplate) {
        this.activeUserTracker = activeUserTracker;
        this.temporaryFileStorageService = temporaryFileStorageService;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("connection event");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String humanReadableChatId = accessor.getFirstNativeHeader("chatId");
        String webSocketChatId = accessor.getSessionId();
        if(humanReadableChatId!=null && webSocketChatId!=null) {
            activeUserTracker.addConnection( webSocketChatId, humanReadableChatId);
            messagingTemplate.convertAndSendToUser(webSocketChatId, "/queue/file",
                    temporaryFileStorageService.getSessionFile(humanReadableChatId));
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("disconnection event");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String webSocketChatId = accessor.getSessionId();
        System.out.println(webSocketChatId);
        if(activeUserTracker.decrementConnection(webSocketChatId) == 0){
            String humanReadableChatId = activeUserTracker.getHumanReadableChatId(webSocketChatId);

            System.out.println(humanReadableChatId);
            System.out.println(webSocketChatId);

            activeUserTracker.removeConnection(webSocketChatId);
            temporaryFileStorageService.removeFile(humanReadableChatId);
        }
    }

}
