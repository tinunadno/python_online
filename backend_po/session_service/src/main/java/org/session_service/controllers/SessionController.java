package org.session_service.controllers;

import jakarta.validation.Valid;
import org.session_service.DTO.CreateSessionRequest;
import org.session_service.DTO.JoinSessionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessionAPI")
public class SessionController {
    @PostMapping("/createSession")
    public ResponseEntity<?> createNewSession(@Valid @RequestBody CreateSessionRequest createSessionRequest) {
        return ResponseEntity.ok(createSessionRequest.getUserId() + "    " + createSessionRequest.getFileId());
    }

    @GetMapping("/joinSession")
    public ResponseEntity<?> joinSession(@Valid @RequestBody JoinSessionRequest joinSessionRequest) {
        return ResponseEntity.ok(joinSessionRequest.getSessionId());
    }
}
