package org.web_socket_service.services;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.web_socket_service.configurations.JwtConfig;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JWTService {

    private final JwtConfig jwtConfig;

    public JWTService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String serviceName, String subject) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, serviceName, subject);
    }

    private String createToken(Map<String, Object> claims, String serviceName, String subject) {
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

    private Claims parseToken(String token) {
        String serviceSecretKey = jwtConfig.getServiceSecretKey("WEB_SOCKET_SERVICE_USER_KEY");
        byte[] keyBytes = serviceSecretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getSessionId(String token) {
        return parseToken(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public boolean validateUserToken(String token, String sessionId) {
        return (getSessionId(token).equals(sessionId) && !isTokenExpired(token));
    }

    private boolean validateTokenWithSecretKey(String token, String secretKey) {
        try {
            byte[] secret = secretKey.getBytes(StandardCharsets.UTF_8);
            JWSObject jwsObject = JWSObject.parse(token);

            JWSVerifier verifier = new MACVerifier(secret);
            return jwsObject.verify(verifier);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateServiceTokenWithSecret(String token) {
        // Получаем секретный ключ для микросервиса
        String secretKey = jwtConfig.getServiceSecretKey("WEB_SOCKET_SERVICE");
        return validateTokenWithSecretKey(token, secretKey);
    }
}
