package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.request.AuthenticationRequest;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.AuthenticationResponse;
import com.example.DocumentManagement.response.MessageResponse;
import com.example.DocumentManagement.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @RequestBody UserCreateRequest userCreateRequest
    ) {
        return ResponseEntity.ok(authenticationService.register(userCreateRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request, response));
    }
}
