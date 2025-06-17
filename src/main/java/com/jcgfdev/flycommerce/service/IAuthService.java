package com.jcgfdev.flycommerce.service;

import com.jcgfdev.flycommerce.payload.request.AuthRequest;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;
import com.jcgfdev.flycommerce.payload.response.AuthResponse;

public interface IAuthService {
    AuthResponse register(RegisterUserRequest registerUserRequest);
    AuthResponse login(AuthRequest authRequest);
}
