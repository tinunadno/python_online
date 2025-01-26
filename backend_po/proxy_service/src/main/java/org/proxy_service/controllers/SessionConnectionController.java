package org.proxy_service.controllers;

import jakarta.validation.Valid;
import org.proxy_service.DTO.*;
import org.proxy_service.components.ServiceProperties;
import org.proxy_service.configurations.JwtConfig;
import org.proxy_service.services.JWTService;
import org.proxy_service.services.RequestSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("pythonOnline/sessionConnection")
public class SessionConnectionController {

    private final RequestSendingService requestSendingService;
    private final ServiceProperties serviceProperties;
    private final JWTService jwtService;
    private final JwtConfig jwtConfig;

    public SessionConnectionController(RequestSendingService requestSendingService, ServiceProperties serviceProperties, JWTService jwtService, JwtConfig jwtConfig) {
        this.requestSendingService = requestSendingService;
        this.serviceProperties = serviceProperties;
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
    }

    //probably it would be better if i'll plug file creation here, but I aint really sure about that
    //TODO add web socket secret key for interaciotn with specific controllers
    //TODO add user webSockets tokens
    @PostMapping("/joinSession")
    public ResponseEntity<?> joinSession(@Valid @RequestBody SessionConnectionRequest sessionConnectionRequest) {
        try {
            ResponseEntity<Map> sessionServiceResponse = requestSendingService.sendGetRequest(serviceProperties.getSessionServiceName(), serviceProperties.getJoinSessionEndpoint(), sessionConnectionRequest);

            if (sessionServiceResponse.getBody() == null || sessionServiceResponse.getBody().get("sessionId") == null) {
                ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            WebSocketConnectionResponse response = new WebSocketConnectionResponse(requestSendingService.getServiceUrl(serviceProperties.getWebSocketServiceName()), sessionServiceResponse.getBody().get("sessionId").toString(),
                    jwtService.generateToken(jwtConfig.getServiceSecretKey("WEB_SOCKET_SERVICE_USER_KEY"), sessionConnectionRequest.getSessionId()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createSession")
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateSessionRequest createSessionRequest) {
        try {
            ResponseEntity<Map> sessionServiceResponse = requestSendingService.sendPostRequest(serviceProperties.getSessionServiceName(), serviceProperties.getCreateSessionEndpoint(), createSessionRequest);
            if (sessionServiceResponse.getBody() == null || sessionServiceResponse.getBody().get("sessionId") == null) {
                ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String sessionId = (String) sessionServiceResponse.getBody().get("sessionId");

            WebSocketConnectionResponse response = new WebSocketConnectionResponse(requestSendingService.getServiceUrl(serviceProperties.getWebSocketServiceName()), sessionId,
                    jwtService.generateToken(sessionId, "WEB_SOCKET_SERVICE_USER_KEY"));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deleteSession")
    public ResponseEntity<?> deleteSession(@Valid @RequestBody DeleteSessionRequest deleteSessionRequest) {
        try {
            ResponseEntity<Map> response = requestSendingService.sendPostRequest(serviceProperties.getSessionServiceName(), serviceProperties.getDeleteSessionEndpoint(), deleteSessionRequest);

            //CloseWebSocketConnectionRequest closeWebSocketConnectionRequest = new CloseWebSocketConnectionRequest(deleteSessionRequest.getSessionId());
            //requestSendingService.sendPostRequest(serviceProperties.getWebSocketServiceName(), serviceProperties.getWebSocketCloseSessionEndpoint(), closeWebSocketConnectionRequest);

            if (response.getBody() == null || response.getBody().get("sessionId") == null) {
                ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
