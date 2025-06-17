package com.jcgfdev.flycommerce.security.util;

import com.jcgfdev.flycommerce.security.model.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class AuthoritiesUtils {

    private AuthoritiesUtils() {
    }

    public static List<SimpleGrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }
}
