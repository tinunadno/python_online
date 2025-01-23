package org.session_service.configurations;

import org.session_service.components.ServiceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtConfig {

    private final Map<String, String> availableKeys = new HashMap<>();

    public JwtConfig(ServiceProperties serviceProperties,
                     @Value("${SESSION_SERVICE_KEY}") String sessionServiceKey,
                     @Value("${FILE_SERVICE_KEY}") String fileServiceKey) {
        availableKeys.put("SESSION_SERVICE", sessionServiceKey);
        availableKeys.put(serviceProperties.getFileServiceName(), fileServiceKey);
    }

    public String getServiceSecretKey(String serviceName) {
        return availableKeys.get(serviceName);
    }
}