package com.jcgfdev.flycommerce.service;

import com.jcgfdev.flycommerce.payload.request.LoginRequest;
import com.jcgfdev.flycommerce.payload.response.LoginResponse;

public interface IAuthService {
    LoginResponse login(LoginRequest loginRequest);
}
