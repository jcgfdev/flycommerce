package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UserDTO;
import com.jcgfdev.flycommerce.exception.AlreadyExistsException;
import com.jcgfdev.flycommerce.exception.DataNotFoundException;
import com.jcgfdev.flycommerce.mapper.IUserMapper;
import com.jcgfdev.flycommerce.model.User;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;
import com.jcgfdev.flycommerce.repository.UserRepository;
import com.jcgfdev.flycommerce.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    //utils
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    //Repository
    private final UserRepository userRepository;
    //messages
    private static final String USEREXISTS = "usuario ya existe";
    private static final String USERNOTEXISTS = "usuario no existe";

    @Override
    public UserDTO register(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
            throw new AlreadyExistsException(USEREXISTS);
        }
        User user = User.builder()
                .username(registerUserRequest.getUsername())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .roles(registerUserRequest.getRoles())
                .build();
        return userMapper.entityToDTO(userRepository.save(user));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(USERNOTEXISTS));
    }
}
