package com.jcgfdev.flycommerce.exception;
import com.jcgfdev.flycommerce.exception.responses.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/test");
    }

    @Test
    void shouldHandleAlreadyExistsException() {
        AlreadyExistsException ex = new AlreadyExistsException("Ya existe");
        ResponseEntity<ErrorResponseDTO> response = handler.handleAlreadyExists(ex, request);
        assertEquals(409, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Dato ya existe. Ya existe", response.getBody().getMessage());
    }

    @Test
    void shouldHandleDataNotFoundException() {
        DataNotFoundException ex = new DataNotFoundException("No encontrado");
        ResponseEntity<ErrorResponseDTO> response = handler.handleNotFound(ex, request);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Dato no encontrado. No encontrado", response.getBody().getMessage());
    }

    @Test
    void shouldHandleJwtException() {
        JwtException ex = new JwtException("Token inválido");
        ResponseEntity<ErrorResponseDTO> response = handler.handleJwtException(ex, request);
        assertEquals(401, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Token inválido", response.getBody().getMessage());
    }

    @Test
    void shouldHandleJwtAuthenticationException() {
        JwtAuthenticationException ex = new JwtAuthenticationException("JWT no válido");
        ResponseEntity<ErrorResponseDTO> response = handler.handleJwtException(ex, request);
        assertEquals(401,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("JWT no válido", response.getBody().getMessage());
    }

    @Test
    void shouldHandleExpiredJwtException() {
        ExpiredJwtException ex = mock(ExpiredJwtException.class);
        ResponseEntity<ErrorResponseDTO> response = handler.handleExpiredToken(ex, request);
        assertEquals(401, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("El token ha expirado", response.getBody().getMessage());
    }

    @Test
    void shouldHandleAuthenticationCredentialsNotFoundException() {
        AuthenticationCredentialsNotFoundException ex = new AuthenticationCredentialsNotFoundException("No auth");
        ResponseEntity<ErrorResponseDTO> response = handler.handleNoAuth(ex, request);
        assertEquals(401, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("No se encontró autenticación en la solicitud", response.getBody().getMessage());
    }

    @Test
    void shouldHandleAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Denegado");
        ResponseEntity<ErrorResponseDTO> response = handler.handleAccessDenied(ex, request);
        assertEquals(403, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Acceso denegado", response.getBody().getMessage());
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new Exception("Error genérico");
        ResponseEntity<ErrorResponseDTO> response = handler.handleGeneralException(ex, request);
        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error genérico", response.getBody().getMessage());
    }
}