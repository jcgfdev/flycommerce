package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.model.jpa.Roles;
import com.jcgfdev.flycommerce.repository.jpa.RolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolesServiceTest {

    @InjectMocks
    private RolesService rolesService;

    @Mock
    private RolesRepository rolesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearRol_whenRoleExists_returnsExistingRole() {
        Roles existingRole = new Roles();
        existingRole.setRol(com.jcgfdev.flycommerce.security.model.Role.USER);

        when(rolesRepository.findByRol(existingRole.getRol())).thenReturn(Optional.of(existingRole));

        Roles result = rolesService.crearRol(existingRole);

        assertEquals(existingRole, result);
        verify(rolesRepository, times(1)).findByRol(existingRole.getRol());
        verify(rolesRepository, never()).save(any(Roles.class));
    }

    @Test
    void crearRol_whenRoleDoesNotExist_savesAndReturnsRole() {
        Roles newRole = new Roles();
        newRole.setRol(com.jcgfdev.flycommerce.security.model.Role.ADMIN);

        when(rolesRepository.findByRol(newRole.getRol())).thenReturn(Optional.empty());
        when(rolesRepository.save(newRole)).thenReturn(newRole);

        Roles result = rolesService.crearRol(newRole);

        assertEquals(newRole, result);
        verify(rolesRepository).findByRol(newRole.getRol());
        verify(rolesRepository).save(newRole);
    }
}