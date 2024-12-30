package org.user_db_service.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.user_db_service.DTO.UserDeleteRequest;
import org.user_db_service.repositories.UserRepository;

@Service
public class UserDeleteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDeleteService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void deleteUser(UserDeleteRequest userDeleteRequest) throws IllegalArgumentException{

        String hashedPassword = passwordEncoder.encode(userDeleteRequest.getPassword());

        int deletionCount = userRepository.deleteUserByUsernameAndPassword(userDeleteRequest.getUsername(), hashedPassword);

        if(deletionCount == 0){
            throw new IllegalArgumentException("invalid username or password");
        }

    }

}
