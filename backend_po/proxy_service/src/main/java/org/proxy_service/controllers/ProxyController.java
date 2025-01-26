package org.proxy_service.controllers;

import org.proxy_service.DTO.ErrorResponse;
import org.proxy_service.components.ServiceProperties;
import org.proxy_service.services.JWTService;
import org.proxy_service.services.RequestSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;

@RestController
@RequestMapping("/pythonOnline")
public class ProxyController {

    private final RequestSendingService requestSendingService;
    private final ServiceProperties serviceProperties;
    private final JWTService jwtService;

    @Autowired
    public ProxyController(RequestSendingService requestSendingService, ServiceProperties serviceProperties, JWTService jwtService) {
        this.requestSendingService = requestSendingService;
        this.serviceProperties = serviceProperties;
        this.jwtService = jwtService;
    }

    //TODO add tokens
    @PostMapping("/authentication/register")
    public ResponseEntity<?> userServiceRegister(@RequestBody Map<String, String> request){
        try {
            ResponseEntity<Map> response = requestSendingService.sendPostRequestProxy(serviceProperties.getAuthServiceName(), serviceProperties.getAuthRegistrationEndpoint(), request);
            //if username doesn't exist in request, httpStatusCodeException will appear, 'cuz request is validated in user service
            response.getBody().put("token", jwtService.generateUserToken(request.get("username")));
            return response;
        }catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("current service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authentication/authorize")
    public ResponseEntity<?> userServiceAuthorize(@RequestBody Map<String, String> request){
        try {
            ResponseEntity<Map> response = requestSendingService.sendPostRequestProxy(serviceProperties.getAuthServiceName(), serviceProperties.getAuthAuthorizationEndpoint(), request);
            //if username doesn't exist in request, httpStatusCodeException will appear, 'cuz request is validated in user service
            response.getBody().put("token", jwtService.generateUserToken(request.get("username")));
            return response;
        }catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("current service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/userManagement/deleteUser")
    public ResponseEntity<?> userServiceDeleteUser(@RequestBody Map<String, String> request){
        try {
            return requestSendingService.sendPostRequestProxy(serviceProperties.getAuthServiceName(), serviceProperties.getUserManagementDeleteUserEndpoint(), request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("authentication service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
