package com.jcgfdev.flycommerce.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.*;

class CustomAuthEntryPointTest {

    @Test
    void shouldSendUnauthorizedResponse() throws Exception {
        CustomAuthEntryPoint entryPoint = new CustomAuthEntryPoint();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);
        // Act
        entryPoint.commence(request, response, authException);
        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }


}