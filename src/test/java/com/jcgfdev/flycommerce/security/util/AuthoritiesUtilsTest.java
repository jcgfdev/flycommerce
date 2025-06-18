package com.jcgfdev.flycommerce.security.util;
import com.jcgfdev.flycommerce.security.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthoritiesUtilsTest {
    @Test
    void shouldConvertRolesToGrantedAuthorities() {
        List<Role> roles = List.of(Role.ADMIN, Role.USER);
        List<? extends GrantedAuthority> authorities = AuthoritiesUtils.mapRolesToAuthorities(roles);
        assertEquals(2, authorities.size());
        List<String> roleNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        assertTrue(roleNames.contains("ROLE_ADMIN"));
        assertTrue(roleNames.contains("ROLE_USER"));
    }

    @Test
    void shouldReturnEmptyListForEmptyRoles() {
        List<Role> roles = List.of();
        List<? extends GrantedAuthority> authorities = AuthoritiesUtils.mapRolesToAuthorities(roles);
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
}