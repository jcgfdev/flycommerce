package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.exception.AlreadyExistsException;
import com.jcgfdev.flycommerce.exception.DataNotFoundException;
import com.jcgfdev.flycommerce.mapper.IUsersMapper;
import com.jcgfdev.flycommerce.model.jpa.Roles;
import com.jcgfdev.flycommerce.model.jpa.Users;
import com.jcgfdev.flycommerce.payload.request.CreateUserRequest;
import com.jcgfdev.flycommerce.repository.jpa.RolesRepository;
import com.jcgfdev.flycommerce.repository.jpa.UserRepository;
import com.jcgfdev.flycommerce.security.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private IUsersMapper usersMapper;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private Roles mockRole;

    @Mock
    private Users mockUser;

    @Mock
    private UsersDTO mockUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_whenUserExists_returnsDto() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(usersMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        UsersDTO result = usersService.findById(id);

        assertNotNull(result);
        assertEquals(mockUserDTO, result);
        verify(userRepository).findById(id);
    }

    @Test
    void findById_whenUserNotFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> usersService.findById(id));
    }

    @Test
    void saveUser_whenEmailExists_throwsAlreadyExistsException() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> usersService.saveUser(request));
    }

    @Test
    void saveUser_whenNewUser_savesSuccessfully() {
        CreateUserRequest request = new CreateUserRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPassword("password");
        request.setRole(Set.of("admin"));

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(encoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(rolesRepository.findByRol(Role.ADMIN)).thenReturn(Optional.of(mockRole));
        when(userRepository.save(any(Users.class))).thenReturn(mockUser);
        when(usersMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        UsersDTO result = usersService.saveUser(request);

        assertNotNull(result);
        assertEquals(mockUserDTO, result);
    }

    @Test
    void findByEmail_whenUserExists_returnsDto() {
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(usersMapper.toDto(mockUser)).thenReturn(mockUserDTO);

        UsersDTO result = usersService.findByEmail(email);

        assertNotNull(result);
        assertEquals(mockUserDTO, result);
    }

    @Test
    void findByEmail_whenUserNotFound_throwsException() {
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> usersService.findByEmail(email));
    }

    @Test
    void resolveRoles_whenNullRoles_returnsDefaultUserRole() {
        when(rolesRepository.findByRol(Role.USER)).thenReturn(Optional.of(mockRole));

        Set<Roles> result = usersServiceTestResolveRolesProxy(null);

        assertTrue(result.contains(mockRole));
    }

    @Test
    void resolveRoles_whenAdminRoleProvided_returnsAdminRole() {
        when(rolesRepository.findByRol(Role.ADMIN)).thenReturn(Optional.of(mockRole));

        Set<Roles> result = usersServiceTestResolveRolesProxy(Set.of("admin"));

        assertTrue(result.contains(mockRole));
    }

    // Proxy para probar el método privado resolveRoles
    private Set<Roles> usersServiceTestResolveRolesProxy(Set<String> roles) {
        try {
            var method = UsersService.class.getDeclaredMethod("resolveRoles", Set.class);
            method.setAccessible(true);
            return (Set<Roles>) method.invoke(usersService, roles);
        } catch (Exception e) {
            fail("Error invoking resolveRoles: " + e.getMessage());
            return Collections.emptySet();
        }
    }
}