package org.proxy_service.controllers;

import org.proxy_service.services.RequestSendingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    //I aint really sure for till now where can i plug it
    private final String servicesAddress = "http://localhost:8080";


    @PostMapping("/authentication/register")
    public ResponseEntity<?> userServiceRegister(Map<String, String> request){
        return requestSendingService.sendPostRequestProxy(servicesAddress+"/authentication/register", request);
    }

    @PostMapping("/authentication/authorize")
    public ResponseEntity<?> userServiceAuthorize(Map<String, String> request){
        return requestSendingService.sendPostRequestProxy(servicesAddress+"/authentication/authorize", request);
    }

    @PostMapping("/userManagement/deleteUser")
    public ResponseEntity<?> userServiceDeleteUser(Map<String, String> request){
        return requestSendingService.sendPostRequestProxy(servicesAddress+"/userManagement/deleteUser", request);
    }



}
