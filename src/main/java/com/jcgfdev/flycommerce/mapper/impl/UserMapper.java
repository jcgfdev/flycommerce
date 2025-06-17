package com.jcgfdev.flycommerce.mapper.impl;

import com.jcgfdev.flycommerce.dto.UserDTO;
import com.jcgfdev.flycommerce.mapper.IUserMapper;
import com.jcgfdev.flycommerce.model.User;
import com.jcgfdev.flycommerce.security.model.Role;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements IUserMapper {
    @Override
    public User dtoToEntity(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .roles(userDTO.getRoles().stream()
                        .map(Role::valueOf)
                        .toList())
                .build();
    }

    @Override
    public UserDTO entityToDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(Enum::name)
                        .toList())
                .build();
    }
}
