package org.user_db_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user_db_service.DTO.UserLoginRequest;
import org.user_db_service.services.UserAuthenticationService;

@RestController
@RequestMapping("/Authentication")
public class AuthenticationController {

    UserAuthenticationService userAuthenticationService;

    public AuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/authorize")
    public ResponseEntity<?> authorize(@Valid @RequestBody UserLoginRequest request) {
        return ResponseEntity.ok().body(userAuthenticationService.double_balls(request));
    }

    @PostMapping("/register")
    public String register() {
        return "registered";
    }
}
