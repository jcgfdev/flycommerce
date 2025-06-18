package com.jcgfdev.flycommerce.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenFilterTest {
    private JwtTokenFilter jwtTokenFilter;
    private JwtProvider jwtProvider;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
        jwtProvider = mock(JwtProvider.class);
        jwtTokenFilter = new JwtTokenFilter(jwtProvider);
    }

    @Test
    void shouldIgnoreRequestWithoutAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        jwtTokenFilter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldIgnoreRequestWithInvalidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid.token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        when(jwtProvider.isTokenValid("invalid.token")).thenReturn(false);
        jwtTokenFilter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateRequestWithValidToken() throws ServletException, IOException {
        String token = "valid.token";
        String username = "usuario@demo.com";
        List<String> roles = List.of("ADMIN", "USER");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtProvider.isTokenValid(token)).thenReturn(true);
        when(jwtProvider.getUsernameFromToken(token)).thenReturn(username);
        when(jwtProvider.getRolesFromToken(token)).thenReturn(roles);

        jwtTokenFilter.doFilterInternal(request, response, chain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(chain, times(1)).doFilter(request, response);
    }

}