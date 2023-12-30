package com.sugardaddy.authenticationservice.service.impl;

import com.sugardaddy.authenticationservice.model.Role;
import com.sugardaddy.authenticationservice.model.User;
import com.sugardaddy.authenticationservice.repository.UserRepository;
import com.sugardaddy.authenticationservice.request.RegisterRequest;
import com.sugardaddy.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity register(RegisterRequest registerRequest) {
        if (Objects.nonNull(registerRequest) && Objects.nonNull(registerRequest.getEmail())
            && Objects.nonNull(registerRequest.getPassword()) && Objects.nonNull(registerRequest.getRole())
                && Objects.nonNull(registerRequest.getMobile()) && Objects.nonNull(registerRequest.getFirstName())
        ) {
            var userExists = userRepository.findByEmail(registerRequest.getEmail());
            if (userExists.isEmpty()) {
                log.info("Creating new user with request: {}", registerRequest);
                User user = new User();
                user.setEmail(registerRequest.getEmail());
                log.info("Password set shuru");
                user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                log.info("password set");
                user.setRole(Role.USER);
                user.setEnabled(false);
                user.setAccountNonExpired(true);
                user.setAccountCreationDate(LocalDateTime.now());
                user.setAccountNonLocked(true);
                user.setCredentialsNonExpired(true);
                user.setFirstName(registerRequest.getFirstName());
                user.setLastName(Objects.nonNull(registerRequest.getLastName()) ? registerRequest.getLastName() : null);
                log.info("trying to save to db");
                userRepository.save(user);
                log.info("Saved success");
                //SEND KAFKA EVENT
                return new ResponseEntity<>("Successfully created user.", HttpStatus.CREATED);
            }
            log.info(String.format("User already exists with same email %s!", registerRequest.getEmail()));
            return new ResponseEntity(String.format("User already exists with same email %s!", registerRequest.getEmail()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void login() {

    }
}
