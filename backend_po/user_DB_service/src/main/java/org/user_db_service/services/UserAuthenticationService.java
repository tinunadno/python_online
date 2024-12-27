package org.user_db_service.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.user_db_service.DTO.UserLoginRequest;
import org.user_db_service.DTO.UserRegisterRequest;
import org.user_db_service.entities.UserEntity;
import org.user_db_service.repositories.UserRepository;

@Service
public class UserAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long authorizeUser(UserLoginRequest userLoginRequest) throws IllegalArgumentException {
        UserEntity user = userRepository.findUserByUsername(userLoginRequest.getUsername());

        if(user == null){
            throw new IllegalArgumentException("invalid username or password");
        }

        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user.getId();
    }

    public Long RegisterUser(UserRegisterRequest userRegisterRequest) {
        String hashedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        UserEntity user = new UserEntity(userRegisterRequest.getUsername(), userRegisterRequest.getEmail(), hashedPassword);
        try {
            UserEntity savedUser = userRepository.save(user);
            return savedUser.getId();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username already exists");
        }
    }
}
