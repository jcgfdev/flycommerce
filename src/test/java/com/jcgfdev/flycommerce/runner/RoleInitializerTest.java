package com.jcgfdev.flycommerce.runner;

import com.jcgfdev.flycommerce.security.model.Role;
import com.jcgfdev.flycommerce.service.IRolesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.ApplicationArguments;

import static org.mockito.Mockito.*;

class RoleInitializerTest {

    @Mock
    private IRolesService rolesService;

    @Mock
    private ApplicationArguments applicationArguments;

    @InjectMocks
    private RoleInitializer roleInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_shouldCreateAllRoles() {
        // Act
        roleInitializer.run(applicationArguments);

        // Assert
        verify(rolesService, times(1)).crearRol(argThat(r -> r.getRol() == Role.USER));
        verify(rolesService, times(1)).crearRol(argThat(r -> r.getRol() == Role.SELLER));
        verify(rolesService, times(1)).crearRol(argThat(r -> r.getRol() == Role.ADMIN));
        verifyNoMoreInteractions(rolesService);
    }
}