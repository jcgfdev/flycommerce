package com.jcgfdev.flycommerce.security.jwt;
import com.jcgfdev.flycommerce.security.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {
    private JwtProvider jwtProvider;

    @BeforeEach
    void setup() {
        JwtProperties props = new JwtProperties();
        props.setSecret("my-secret-key-which-should-be-very-long-and-secure");
        props.setExpiration(3600000L);
        jwtProvider = new JwtProvider(props);
        jwtProvider.init();
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String email = "test@test.com";
        List<Role> roles = List.of(Role.ADMIN, Role.USER);
        String token = jwtProvider.generateToken(email, roles);
        assertNotNull(token);
        assertTrue(jwtProvider.isTokenValid(token));
        String extractedEmail = jwtProvider.getUsernameFromToken(token);
        List<String> extractedRoles = jwtProvider.getRolesFromToken(token);
        assertEquals(email, extractedEmail);
        assertTrue(extractedRoles.contains("ADMIN"));
        assertTrue(extractedRoles.contains("USER"));
    }

    @Test
    void shouldInvalidateExpiredToken() {
        JwtProperties props = new JwtProperties();
        props.setSecret("my-secret-key-which-should-be-very-long-and-secure");
        props.setExpiration(1L);
        jwtProvider = new JwtProvider(props);
        jwtProvider.init();
        String token = jwtProvider.generateToken("test2@test.com", List.of(Role.USER));
        assertFalse(jwtProvider.isTokenValid(token));
    }

}