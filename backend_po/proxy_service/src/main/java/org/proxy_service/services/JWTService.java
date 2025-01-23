package org.proxy_service.services;

import io.jsonwebtoken.SignatureAlgorithm;
import org.proxy_service.configurations.JwtConfig;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private final JwtConfig jwtConfig;

    public JWTService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String subject, String serviceName){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, subject, serviceName);
    }

    private String createToken(Map<String, Object> claims, String subject, String serviceName) {
        String serviceSecretKey = jwtConfig.getServiceSecretKey(serviceName);
        byte[] keyBytes = serviceSecretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
