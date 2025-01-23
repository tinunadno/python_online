package org.proxy_service.configurations;

import org.proxy_service.components.ServiceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtConfig {

    private final Map<String, String> availableKeys = new HashMap<>();

    public JwtConfig(ServiceProperties serviceProperties,
                     @Value("${USER_DB_SERVICE_KEY}") String userDBServiceKey,
                     @Value("${SESSION_SERVICE_KEY}") String sessionServiceKey) {
        availableKeys.put(serviceProperties.getAuthServiceName(), userDBServiceKey);
        availableKeys.put(serviceProperties.getSessionServiceName(), sessionServiceKey);
    }

    public String getServiceSecretKey(String serviceName) {
        return availableKeys.get(serviceName);
    }
}
