package org.user_db_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user_db_service.DTO.responses.OkResponse;
import org.user_db_service.DTO.requsts.UserLoginRequest;
import org.user_db_service.DTO.requsts.UserRegisterRequest;
import org.user_db_service.services.UserAuthenticationService;
import org.user_db_service.DTO.responses.ErrorResponse;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    UserAuthenticationService userAuthenticationService;

    public AuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/authorize")
    public ResponseEntity<?> authorize(@Valid @RequestBody UserLoginRequest request) {
        try {
            Long userId = userAuthenticationService.authorizeUser(request);
            OkResponse response = new OkResponse(userId.toString());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest request) {
        try {
                Long userId = userAuthenticationService.RegisterUser(request);
            OkResponse response = new OkResponse(userId.toString());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
