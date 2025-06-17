package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UserDTO;
import com.jcgfdev.flycommerce.exception.JwtAuthenticationException;
import com.jcgfdev.flycommerce.model.User;
import com.jcgfdev.flycommerce.payload.request.AuthRequest;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;
import com.jcgfdev.flycommerce.payload.response.AuthResponse;
import com.jcgfdev.flycommerce.security.jwt.JwtProvider;
import com.jcgfdev.flycommerce.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserService userService;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_successful() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("juan");
        request.setRoles(List.of());
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("juan");
        userDTO.setRoles(List.of());
        when(userService.register(request)).thenReturn(userDTO);
        when(jwtProvider.generateToken("juan", List.of())).thenReturn("token123");

        AuthResponse response = authService.register(request);

        assertEquals("juan", response.getUsername());
        assertEquals("token123", response.getToken());
    }

    @Test
    void login_successful() {
        AuthRequest request = new AuthRequest();
        request.setUsername("juan");
        request.setPassword("123");
        User user = new User();
        user.setUsername("juan");
        user.setRoles(List.of());
        when(userService.findByUsername("juan")).thenReturn(user);
        when(jwtProvider.generateToken("juan", List.of())).thenReturn("token123");

        AuthResponse response = authService.login(request);

        assertEquals("juan", response.getUsername());
        assertEquals("token123", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_failure() {
        AuthRequest request = new AuthRequest();
        request.setUsername("juan");
        request.setPassword("123");

        doThrow(new AuthenticationException("Bad credentials") {
        }).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(JwtAuthenticationException.class, () -> authService.login(request));
    }
}