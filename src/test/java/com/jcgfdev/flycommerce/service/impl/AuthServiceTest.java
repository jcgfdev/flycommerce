package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.payload.request.LoginRequest;
import com.jcgfdev.flycommerce.payload.response.LoginResponse;
import com.jcgfdev.flycommerce.security.jwt.JwtProvider;
import com.jcgfdev.flycommerce.service.IUsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private IUsersService usersService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Limpiar contexto de seguridad antes de cada test
        SecurityContextHolder.clearContext();
    }

    @Test
    void login_validCredentials_returnsLoginResponse() {
        // Arrange
        var loginRequest = new LoginRequest();
        loginRequest.setUser("test@example.com");
        loginRequest.setPassword("password");

        var user = new UsersDTO();
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRoles(Set.of("ADMIN", "USER"));

        var authMock = mock(Authentication.class);

        when(usersService.findByEmail("test@example.com")).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        when(jwtProvider.generateToken(eq("test@example.com"), anyList())).thenReturn("mockedToken");

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test@example.com", response.getUser());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertEquals("mockedToken", response.getToken());

        // Verificar que se autenticó correctamente
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        var token = captor.getValue();
        assertEquals("test@example.com", token.getPrincipal());
        assertEquals("password", token.getCredentials());

        // Verificar que se estableció el contexto de seguridad
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertSame(authMock, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void login_userNotFound_throwsDataNotFoundException() {
        // Arrange
        var loginRequest = new LoginRequest();
        loginRequest.setUser("nonexistent@example.com");
        loginRequest.setPassword("password");

        when(usersService.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act + Assert
        var exception = assertThrows(SecurityException.class, () -> authService.login(loginRequest));
        assertTrue(exception.getMessage().contains("usuario no existe"));
    }

    @Test
    void login_authenticationFails_throwsSecurityException() {
        // Arrange
        var loginRequest = new LoginRequest();
        loginRequest.setUser("test@example.com");
        loginRequest.setPassword("wrongpassword");

        var user = new UsersDTO();
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRoles(Set.of("ADMIN"));

        when(usersService.findByEmail("test@example.com")).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        // Act + Assert
        var exception = assertThrows(SecurityException.class, () -> authService.login(loginRequest));
        assertTrue(exception.getMessage().contains("Bad credentials"));
    }
}