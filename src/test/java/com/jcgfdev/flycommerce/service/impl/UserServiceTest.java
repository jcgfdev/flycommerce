package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UserDTO;
import com.jcgfdev.flycommerce.exception.AlreadyExistsException;
import com.jcgfdev.flycommerce.exception.DataNotFoundException;
import com.jcgfdev.flycommerce.mapper.IUserMapper;
import com.jcgfdev.flycommerce.model.User;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;
import com.jcgfdev.flycommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IUserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void register_successful() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("juan");
        request.setPassword("pass123");
        request.setRoles(List.of()); // añade roles de prueba

        when(userRepository.existsByUsername("juan")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("juan");
        userDTO.setRoles(List.of());
        when(userMapper.entityToDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.register(request);

        assertEquals("juan", result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_userAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("juan");

        when(userRepository.existsByUsername("juan")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.register(request));
    }

    @Test
    void findByUsername_successful() {
        User user = new User();
        user.setUsername("juan");
        when(userRepository.findByUsername("juan")).thenReturn(Optional.of(user));
        User result = userService.findByUsername("juan");
        assertEquals("juan", result.getUsername());
    }

    @Test
    void findByUsername_notFound() {
        when(userRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.findByUsername("noexiste"));
    }
}