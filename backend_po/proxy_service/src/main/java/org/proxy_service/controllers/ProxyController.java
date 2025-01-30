package org.proxy_service.controllers;

import org.proxy_service.DTO.ErrorResponse;
import org.proxy_service.DTO.userDBServiceDTO.UserDeleteRequest;
import org.proxy_service.DTO.userDBServiceDTO.UserLoginRequest;
import org.proxy_service.DTO.userDBServiceDTO.UserRegisterRequest;
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
    public ResponseEntity<?> userServiceRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        try {
            ResponseEntity<Map> response = requestSendingService.sendPostRequest(serviceProperties.getAuthServiceName(), serviceProperties.getAuthRegistrationEndpoint(), userRegisterRequest);
            //if username doesn't exist in request, httpStatusCodeException will appear, 'cuz request is validated in user service
            response.getBody().put("token", jwtService.generateUserToken(userRegisterRequest.getUsername()));
            return response;
        }catch (HttpStatusCodeException e){
            return new ResponseEntity<>(new ErrorResponse("service responsed with code " + e.getStatusCode() +"\nerror message: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(new ErrorResponse("service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authentication/authorize")
    public ResponseEntity<?> userServiceAuthorize(@RequestBody UserLoginRequest userLoginRequest){
        try {
            ResponseEntity<Map> response = requestSendingService.sendPostRequest(serviceProperties.getAuthServiceName(), serviceProperties.getAuthAuthorizationEndpoint(), userLoginRequest);
            //if username doesn't exist in request, httpStatusCodeException will appear, 'cuz request is validated in user service
            response.getBody().put("token", jwtService.generateUserToken(userLoginRequest.getUsername()));
            return response;
        }catch (HttpStatusCodeException e){
            return new ResponseEntity<>(new ErrorResponse("service responsed with code " + e.getStatusCode() +"\nerror message: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(new ErrorResponse("service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/userManagement/deleteUser")
    public ResponseEntity<?> userServiceDeleteUser(@RequestBody UserDeleteRequest userDeleteRequest){
        try {
            return requestSendingService.sendPostRequest(serviceProperties.getAuthServiceName(), serviceProperties.getUserManagementDeleteUserEndpoint(), userDeleteRequest);
        }catch (HttpStatusCodeException e){
            return new ResponseEntity<>(new ErrorResponse("service responsed with code " + e.getStatusCode() +"\nerror message: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(new ErrorResponse("service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
