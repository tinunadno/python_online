package org.proxy_service.controllers;

import org.proxy_service.DTO.ErrorResponse;
import org.proxy_service.services.RequestSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pythonOnline")
public class ProxyController {

    @Autowired
    RequestSendingService requestSendingService;
    //TODO add normal service address storage
    private final String userServiceAddress = "http://localhost:8084";


    @PostMapping("/authentication/register")
    public ResponseEntity<?> userServiceRegister(Map<String, String> request){
        try {
            return requestSendingService.sendPostRequestProxy(userServiceAddress + "/authentication/register", request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("authentication service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authentication/authorize")
    public ResponseEntity<?> userServiceAuthorize(Map<String, String> request){
        try {
            return requestSendingService.sendPostRequestProxy(userServiceAddress + "/authentication/authorize", request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("authentication service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/userManagement/deleteUser")
    public ResponseEntity<?> userServiceDeleteUser(Map<String, String> request){
        try {
            return requestSendingService.sendPostRequestProxy(userServiceAddress + "/userManagement/deleteUser", request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("authentication service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
