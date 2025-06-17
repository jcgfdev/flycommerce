package com.jcgfdev.flycommerce.controller;

import com.jcgfdev.flycommerce.docs.AuthApiDocs;
import com.jcgfdev.flycommerce.payload.request.AuthRequest;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;
import com.jcgfdev.flycommerce.payload.response.AuthResponse;
import com.jcgfdev.flycommerce.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        AuthResponse response = authService.register(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }
}
