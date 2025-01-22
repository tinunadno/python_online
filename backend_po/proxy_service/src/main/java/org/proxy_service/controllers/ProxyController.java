package org.proxy_service.controllers;

import org.proxy_service.DTO.ErrorResponse;
import org.proxy_service.services.JWTService;
import org.proxy_service.services.RequestSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pythonOnline")
public class ProxyController {

    @Autowired
    private RequestSendingService requestSendingService;
    @Autowired
    private JWTService jwtService;
    //TODO add normal service address storage
    String userServiceAddress = "USER_DB_SERVICE";


    //TODO add tokens
    @PostMapping("/authentication/register")
    public ResponseEntity<?> userServiceRegister(@RequestBody Map<String, String> request){
        try {
            return requestSendingService.sendPostRequestProxy(userServiceAddress, "/authentication/register", request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("error appeared while sending request to authentication service: "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authentication/authorize")
    public ResponseEntity<?> userServiceAuthorize(@RequestBody Map<String, String> request){
        try {
            return requestSendingService.sendPostRequestProxy(userServiceAddress, "/authentication/authorize", request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("authentication service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/userManagement/deleteUser")
    public ResponseEntity<?> userServiceDeleteUser(@RequestBody Map<String, String> request){
        try {
            return requestSendingService.sendPostRequestProxy(userServiceAddress, "/userManagement/deleteUser", request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("authentication service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
