package com.jcgfdev.flycommerce.mapper.impl;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.model.jpa.Roles;
import com.jcgfdev.flycommerce.model.jpa.Users;
import com.jcgfdev.flycommerce.security.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UsersMapperTest {

    private UsersMapper usersMapper;

    @BeforeEach
    void setUp() {
        usersMapper = new UsersMapper();
    }

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        UUID userId = UUID.randomUUID();
        Users user = new Users();
        user.setId(userId);
        user.setFirstName("Juan");
        user.setLastName("Gutierrez");
        user.setEmail("juan@example.com");
        user.setLocked(false);
        user.setEnabled(true);
        Roles roleAdmin = new Roles();
        roleAdmin.setRol(Role.ADMIN);
        Roles roleUser = new Roles();
        roleUser.setRol(Role.USER);
        user.setRoles(Set.of(roleAdmin, roleUser));
        // Act
        UsersDTO dto = usersMapper.toDto(user);
        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(userId);
        assertThat(dto.getFirstName()).isEqualTo("Juan");
        assertThat(dto.getLastName()).isEqualTo("Gutierrez");
        assertThat(dto.getEmail()).isEqualTo("juan@example.com");
        assertThat(dto.getLocked()).isFalse();
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getRoles()).containsExactlyInAnyOrder("ADMIN", "USER");
    }

    @Test
    void toDto_shouldHandleNullRoles() {
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setFirstName("Juan");
        user.setLastName("Gutierrez");
        user.setEmail("juan@example.com");
        user.setLocked(true);
        user.setEnabled(false);
        user.setRoles(null);
        UsersDTO dto = usersMapper.toDto(user);
        assertThat(dto).isNotNull();
        assertThat(dto.getRoles()).isEmpty();
    }

    @Test
    void toDto_shouldHandleRolesWithNullRol() {
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setFirstName("Juan");
        user.setLastName("Gutierrez");
        user.setEmail("juan@example.com");
        user.setLocked(false);
        user.setEnabled(true);
        Roles roleWithNullRol = new Roles();
        roleWithNullRol.setRol(null);
        user.setRoles(Set.of(roleWithNullRol));
        UsersDTO dto = usersMapper.toDto(user);
        assertThat(dto).isNotNull();
        assertThat(dto.getRoles()).containsExactly((String) null);
    }
}