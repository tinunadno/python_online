package org.file_service.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtConfig {

    private final Map<String, String> availableKeys = new HashMap<>();

    public JwtConfig(@Value("${FILE_SERVICE_KEY}") String fileServiceKey) {
        availableKeys.put("FILE_SERVICE_KEY", fileServiceKey);
    }

    public String getServiceSecretKey(String serviceName) {
        return availableKeys.get(serviceName);
    }
}