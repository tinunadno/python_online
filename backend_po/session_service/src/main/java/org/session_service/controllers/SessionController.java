package org.session_service.controllers;

import jakarta.validation.Valid;
import org.session_service.DTO.CreateSessionRequest;
import org.session_service.DTO.ErrorResponse;
import org.session_service.DTO.JoinSessionRequest;
import org.session_service.DTO.OkResponse;
import org.session_service.services.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessionAPI")
public class SessionController {

    SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/createSession")
    public ResponseEntity<?> createNewSession(@Valid @RequestBody CreateSessionRequest createSessionRequest) {
        try{
            Integer sessionId = sessionService.saveSession(createSessionRequest);
            OkResponse response = new OkResponse(sessionId.toString());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/joinSession")
    public ResponseEntity<?> joinSession(@Valid @RequestBody JoinSessionRequest joinSessionRequest) {
        try{
            Integer sessionId = sessionService.JoinSession(joinSessionRequest);
            OkResponse response = new OkResponse(sessionId.toString());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
