package org.web_socket_service.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.web_socket_service.services.JWTService;

import java.io.IOException;
import java.util.Collections;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    public TokenFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//
//        String token = authorizationHeader.substring(7);
//
//        String requestURI = request.getRequestURI();
//        //i don't know, how else could i separate token validation for different routes
//        if (requestURI.startsWith("/ws/") || requestURI.startsWith("/app/") ||
//                requestURI.startsWith("/webSocketServiceController/executeFile/") ||
//                requestURI.startsWith("/webSocketServiceController/getFileContent/")) {
//            String username = jwtService.getSessionId(token);
//
//            if (username != null && jwtService.validateUserToken(token, username)) {
//
//                var authentication = new UsernamePasswordAuthenticationToken(
//                        username,
//                        null,
//                        Collections.singletonList(new SimpleGrantedAuthority("USER"))
//                );
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//            filterChain.doFilter(request, response);
//        } else if (requestURI.startsWith("/webSocketServiceController/callback/") ||
//                requestURI.startsWith("/webSocketServiceController/removeSessionById")) {
//
//            if (jwtService.validateServiceTokenWithSecret(token)) {
//                filterChain.doFilter(request, response);
//            } else {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            }
//
//        } else {
//            filterChain.doFilter(request, response);
//        }
       filterChain.doFilter(request, response);
    }
}
