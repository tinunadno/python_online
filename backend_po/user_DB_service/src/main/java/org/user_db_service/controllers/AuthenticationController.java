package org.user_db_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user_db_service.DTO.UserLoginRequest;
import org.user_db_service.DTO.UserRegisterRequest;
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
        try {
            Long id = userAuthenticationService.authorizeUser(request);
            return ResponseEntity.ok(id);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest request) {
        try {
            return ResponseEntity.ok(userAuthenticationService.RegisterUser(request));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
