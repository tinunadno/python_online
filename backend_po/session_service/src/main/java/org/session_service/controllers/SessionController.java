package org.session_service.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.session_service.DTO.*;
import org.session_service.services.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
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
    public ResponseEntity<?> joinSession(@RequestParam("sessionId") @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digit integer") String sessionIdRequest) {
        try{
            Integer sessionId = sessionService.JoinSession(sessionIdRequest);
            OkResponse response = new OkResponse(sessionId.toString());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/getSessionFileId")
    public ResponseEntity<?> getFileSessionId(@RequestParam("sessionId") @Pattern(regexp = "^[0-9]{6}$", message = "session id must be 6 digit integer") String sessionId) {
        try{
            String fileSessionId = sessionService.getSessionFileId(sessionId);
            OkResponse response = new OkResponse(fileSessionId);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/deleteSession")
    public ResponseEntity<?> deleteSession(@Valid @RequestBody DeleteSessionRequest deleteSessionRequest){
        try{
            sessionService.removeSession(deleteSessionRequest);
            return ResponseEntity.ok().body(new OkResponse("Session successfully deleted"));
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
