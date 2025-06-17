package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UserDTO;
import com.jcgfdev.flycommerce.exception.JwtAuthenticationException;
import com.jcgfdev.flycommerce.model.User;
import com.jcgfdev.flycommerce.payload.request.AuthRequest;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;
import com.jcgfdev.flycommerce.payload.response.AuthResponse;
import com.jcgfdev.flycommerce.security.jwt.JwtProvider;
import com.jcgfdev.flycommerce.security.model.Role;
import com.jcgfdev.flycommerce.service.IAuthService;
import com.jcgfdev.flycommerce.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterUserRequest registerUserRequest) {
        UserDTO userDTO = userService.register(registerUserRequest);
        String token = jwtProvider.generateToken(userDTO.getUsername(), userDTO.getRoles().stream().map(Role::valueOf).toList());
        return new AuthResponse(userDTO.getUsername(), token);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            User user = userService.findByUsername(authRequest.getUsername());
            String token = jwtProvider.generateToken(user.getUsername(), user.getRoles());
            return new AuthResponse(user.getUsername(), token);
        } catch (AuthenticationException e) {
            throw new JwtAuthenticationException(e.getMessage());
        }
    }
}
