package org.user_db_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.user_db_service.DTO.ErrorResponse;
import org.user_db_service.DTO.OkResponse;
import org.user_db_service.DTO.UserDeleteRequest;
import org.user_db_service.services.UserDeleteService;

@RestController
@RequestMapping("/userManagement")
public class UserDeleteController {

    private final UserDeleteService userDeleteService;

    public UserDeleteController(UserDeleteService userDeleteService) {
        this.userDeleteService = userDeleteService;
    }

    @PostMapping("/deleteUser")
    ResponseEntity<?> deleteUser(@Valid @RequestBody UserDeleteRequest userDeleteRequest){
        try{
            userDeleteService.deleteUser(userDeleteRequest);
            return ResponseEntity.ok(new OkResponse("User deleted successfully"));
        }catch(IllegalArgumentException e){
            ErrorResponse response = new ErrorResponse("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
