package org.user_db_service.services;

import org.springframework.stereotype.Service;
import org.user_db_service.DTO.UserLoginRequest;

@Service
public class UserAuthenticationService {

    public boolean authorizeUser(UserLoginRequest userLoginRequest) {
        return true;
    }

    public boolean RegisterUser(UserLoginRequest userLoginRequest) {
        return true;
    }
}
