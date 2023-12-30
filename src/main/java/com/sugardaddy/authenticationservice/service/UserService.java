package com.sugardaddy.authenticationservice.service;

import com.sugardaddy.authenticationservice.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity register(RegisterRequest registerRequest);
    void login();
}