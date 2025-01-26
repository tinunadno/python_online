package org.web_socket_service.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.web_socket_service.components.ServiceProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtConfig {

    private final Map<String, String> availableKeys = new HashMap<>();

    public JwtConfig(ServiceProperties serviceProperties,
                     @Value("${WEB_SOCKET_SERVICE_KEY}") String webSocketServiceKey,
                     @Value("${FILE_SERVICE_KEY}") String userDBServiceKey,
                     @Value("${SESSION_SERVICE_KEY}") String sessionServiceKey,
                     @Value("${EXECUTION_SERVICE_KEY}") String executionServiceKey,
                     @Value("${WEB_SOCKET_SERVICE_USER_KEY}") String webSocketServiceUserKey) {
        availableKeys.put("WEB_SOCKET_SERVICE", webSocketServiceKey);
        availableKeys.put(serviceProperties.getFileServiceName(), userDBServiceKey);
        availableKeys.put(serviceProperties.getSessionServiceName(), sessionServiceKey);
        availableKeys.put(serviceProperties.getExecutionServiceName(), executionServiceKey);
        availableKeys.put("WEB_SOCKET_SERVICE_USER_KEY", webSocketServiceUserKey);
    }

    public String getServiceSecretKey(String serviceName) {
        return availableKeys.get(serviceName);
    }
}
