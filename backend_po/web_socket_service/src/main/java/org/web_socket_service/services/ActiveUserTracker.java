package org.web_socket_service.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ActiveUserTracker {

    private final ConcurrentHashMap<String, SessionInstance> activeConnections = new ConcurrentHashMap<>();

    public void addConnection(String webSocketChatId, String humanReadableChatId) {
        if(!activeConnections.containsKey(webSocketChatId)) {
            activeConnections.put(webSocketChatId, new SessionInstance(humanReadableChatId));
        }
        activeConnections.get(webSocketChatId).addUser();
    }


    public int decrementConnection(String chatId) {
        if(activeConnections.containsKey(chatId)) {
            return activeConnections.get(chatId).removeUser();
        }
        return -1;
    }

    public String getHumanReadableChatId(String webSocketChatId) {
        if(activeConnections.containsKey(webSocketChatId)) {
            return activeConnections.get(webSocketChatId).getChatId();
        }
        return null;
    }

    public void removeConnection(String webSocketChatId) {
        activeConnections.remove(webSocketChatId);
    }

    private static class SessionInstance{
        private final AtomicInteger activeConnectionsCount;
        private final String chatId;

        public SessionInstance(String chatId) {
            this.chatId = chatId;
            activeConnectionsCount = new AtomicInteger(0);
        }

        public void addUser(){
            this.activeConnectionsCount.incrementAndGet();
        }

        public int removeUser(){
            return this.activeConnectionsCount.decrementAndGet();
        }

        public String getChatId() {
            return chatId;
        }
    }
}
