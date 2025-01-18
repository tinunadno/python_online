package org.web_socket_service.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ActiveUserTracker {

    private final ConcurrentHashMap<String, AtomicInteger> activeConnections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> sessionIds = new ConcurrentHashMap<>();

    public void addConnection(String webSocketSessionId, String humanReadableId) {
        if(!activeConnections.containsKey(humanReadableId)) {
            activeConnections.put(humanReadableId, new AtomicInteger(0));
            sessionIds.put(webSocketSessionId, humanReadableId);
        }
        activeConnections.get(humanReadableId).incrementAndGet();
        sessionIds.put(webSocketSessionId, humanReadableId);
    }


    public int decrementConnection(String webSocketSessionId) {
        if(sessionIds.containsKey(webSocketSessionId)) {
            String humanReadableId = sessionIds.get(webSocketSessionId);
            if(activeConnections.containsKey(humanReadableId)) {
                return activeConnections.get(humanReadableId).decrementAndGet();
            }
        }
        return -1;
    }

    public String getHumanReadableSessionId(String webSocketSessionId) {
        return sessionIds.get(webSocketSessionId);
    }

    public void removeConnection(String webSocketSessionId) {
        if(sessionIds.containsKey(webSocketSessionId)) {
            String humanReadableId = sessionIds.get(webSocketSessionId);
            sessionIds.remove(webSocketSessionId);
            activeConnections.remove(humanReadableId);
        }
    }
}
