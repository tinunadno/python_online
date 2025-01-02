package org.proxy_service.controllers;

import jakarta.validation.Valid;
import org.proxy_service.DTO.CreateSessionRequest;
import org.proxy_service.DTO.DeleteSessionRequest;
import org.proxy_service.DTO.ErrorResponse;
import org.proxy_service.DTO.SessionConnectionRequest;
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

    //TODO create web socket service and add web socket connection logic here

    @Autowired
    RequestSendingService requestSendingService;
    //I aint really sure for till now where can i plug it
    private final String servicesAddress = "http://localhost:8080";

    @PostMapping("/joinSession")
    public ResponseEntity<?> joinSession(@Valid @RequestBody SessionConnectionRequest sessionConnectionRequest) {
        ResponseEntity<Map> response = requestSendingService.sendGetRequest(servicesAddress+"/sessionAPI/joinSession", sessionConnectionRequest);

        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        if(response.getBody() == null || response.getBody().get("sessionId") == null){
            ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        //web socket connection logic

        return null;
    }

    @PostMapping("/createSession")
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateSessionRequest createSessionRequest) {

        ResponseEntity<Map> response = requestSendingService.sendPostRequest(servicesAddress+"/sessionAPI/createSession", createSessionRequest);
        if(response.getStatusCode() == HttpStatus.BAD_REQUEST){
            return response;
        }
        if(response.getBody() == null || response.getBody().get("sessionId") == null){
            ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String sessionId = (String) response.getBody().get("sessionId");

        //some web socket connection logic

        return null;

    }

    @PostMapping("/deleteSession")
    public ResponseEntity<?> deleteSession(@Valid @RequestBody DeleteSessionRequest deleteSessionRequest) {
        ResponseEntity<Map> response = requestSendingService.sendPostRequest(servicesAddress+"/sessionAPI/deleteSession", deleteSessionRequest);
        if(response.getStatusCode() == HttpStatus.BAD_REQUEST){
            return response;
        }
        if(response.getBody() == null || response.getBody().get("sessionId") == null){
            ErrorResponse errorResponse = new ErrorResponse("session service answered with invalid response");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        //probably some more web socket stuff

        return null;
    }

}
