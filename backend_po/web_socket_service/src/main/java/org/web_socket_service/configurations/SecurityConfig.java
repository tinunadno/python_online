package org.web_socket_service.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.web_socket_service.components.TokenFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {
    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter userTokenFilter) {
        this.tokenFilter = userTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers( "/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/ws/**",
                                "/app/**",
                                "/webSocketServiceController/executeFile/**",
                                "/webSocketServiceController/getFileContent/**",
                                "/webSocketServiceController/callback/**",
                                "/webSocketServiceController/removeSessionById").
                        authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}