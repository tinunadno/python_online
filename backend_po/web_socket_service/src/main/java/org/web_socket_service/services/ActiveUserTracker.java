package org.web_socket_service.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ActiveUserTracker {

    private final ConcurrentHashMap<String, SessionInstance> activeConnections = new ConcurrentHashMap<>();

    public void addConnection(String webSocketSessionId, String humanReadableSessionId) {
        if(!activeConnections.containsKey(webSocketSessionId)) {
            activeConnections.put(webSocketSessionId, new SessionInstance(humanReadableSessionId));
        }
        activeConnections.get(webSocketSessionId).addUser();
    }


    public int decrementConnection(String sessionId) {
        if(activeConnections.containsKey(sessionId)) {
            return activeConnections.get(sessionId).removeUser();
        }
        return -1;
    }

    public String getHumanReadableSessionId(String webSocketSessionId) {
        if(activeConnections.containsKey(webSocketSessionId)) {
            return activeConnections.get(webSocketSessionId).getSessionId();
        }
        return null;
    }

    public void removeConnection(String webSocketSessionId) {
        activeConnections.remove(webSocketSessionId);
    }

    private static class SessionInstance{
        private final AtomicInteger activeConnectionsCount;
        private final String sessionId;

        public SessionInstance(String sessionId) {
            this.sessionId = sessionId;
            activeConnectionsCount = new AtomicInteger(0);
        }

        public void addUser(){
            this.activeConnectionsCount.incrementAndGet();
        }

        public int removeUser(){
            return this.activeConnectionsCount.decrementAndGet();
        }

        public String getSessionId() {
            return sessionId;
        }
    }
}
