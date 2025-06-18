package com.jcgfdev.flycommerce.mapper.impl;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.mapper.IUsersMapper;
import com.jcgfdev.flycommerce.model.jpa.Users;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsersMapper implements IUsersMapper {
    @Override
    public UsersDTO toDto(Users user) {
        UsersDTO dto = new UsersDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setLocked(user.getLocked());
        dto.setEnabled(user.getEnabled());
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(r -> r.getRol() != null ? r.getRol().name() : null)
                    .collect(Collectors.toSet()));
        } else {
            dto.setRoles(Set.of());
        }
        return dto;
    }
}
