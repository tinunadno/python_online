package org.execution_service.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtConfig {
    private final Map<String, String> availableKeys = new HashMap<>();

    public JwtConfig(@Value("${EXECUTION_SERVICE_KEY}") String userDBServiceKey) {
        availableKeys.put("EXECUTION_SERVICE", userDBServiceKey);
    }

    public String getServiceSecretKey(String serviceName) {
        return availableKeys.get(serviceName);
    }
}
