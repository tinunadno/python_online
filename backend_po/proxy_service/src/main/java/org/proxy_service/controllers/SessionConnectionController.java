package org.proxy_service.controllers;

import jakarta.validation.Valid;
import org.proxy_service.DTO.*;
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


    @Autowired
    RequestSendingService requestSendingService;
    //TODO add this stuff storage or smth
    private final String sessionServiceAddress = "http://localhost:8081";
    private final String webSocketServiceAddress = "http://localhost:8080";

    //probably it would be better if i'll plug file creation here, but I aint really sure about that
    //TODO add user webSockets tokens
    @PostMapping("/joinSession")
    public ResponseEntity<?> joinSession(@Valid @RequestBody SessionConnectionRequest sessionConnectionRequest) {
        try {
            ResponseEntity<Map> sessionServiceResponse = requestSendingService.sendGetRequest(sessionServiceAddress + "/sessionAPI/joinSession", sessionConnectionRequest);

            if (sessionServiceResponse.getBody() == null || sessionServiceResponse.getBody().get("sessionId") == null) {
                ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            WebSocketConnectionResponse response = new WebSocketConnectionResponse(webSocketServiceAddress, sessionServiceResponse.getBody().get("sessionId").toString());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createSession")
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateSessionRequest createSessionRequest) {
        try {
            ResponseEntity<Map> sessionServiceResponse = requestSendingService.sendPostRequest(sessionServiceAddress + "/sessionAPI/createSession", createSessionRequest);
            if (sessionServiceResponse.getBody() == null || sessionServiceResponse.getBody().get("sessionId") == null) {
                ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String sessionId = (String) sessionServiceResponse.getBody().get("sessionId");

            WebSocketConnectionResponse response = new WebSocketConnectionResponse(webSocketServiceAddress, sessionId);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO add some validation here, 'cuz even if its not an owner, it will kick all user
    @PostMapping("/deleteSession")
    public ResponseEntity<?> deleteSession(@Valid @RequestBody DeleteSessionRequest deleteSessionRequest) {

//        CloseWebSocketConnectionRequest closeWebSocketConnectionRequest = new CloseWebSocketConnectionRequest(deleteSessionRequest.getSessionId());
//
//        try {
//            requestSendingService.sendPostRequest(webSocketServiceAddress + "/webSocketServiceController/removeSessionById", closeWebSocketConnectionRequest);
//        } catch (RuntimeException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        try {
            ResponseEntity<Map> response = requestSendingService.sendPostRequest(sessionServiceAddress + "/sessionAPI/deleteSession", deleteSessionRequest);


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
