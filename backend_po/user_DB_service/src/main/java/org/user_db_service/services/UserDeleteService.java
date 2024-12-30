package org.user_db_service.services;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.user_db_service.DTO.UserDeleteRequest;
import org.user_db_service.entities.UserEntity;
import org.user_db_service.repositories.UserRepository;

@Service
public class UserDeleteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDeleteService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void deleteUser(UserDeleteRequest userDeleteRequest) throws IllegalArgumentException{

        UserEntity userEntity = userRepository.findUserByUsername(userDeleteRequest.getUsername());

        if(userEntity == null) {
            throw new IllegalArgumentException("User with this name doesn't exist");
        }

        if(passwordEncoder.matches(userDeleteRequest.getPassword(), userEntity.getPassword())) {
            userRepository.delete(userEntity);
        }else{
            throw new IllegalArgumentException("Passwords do not match");
        }

    }

}
