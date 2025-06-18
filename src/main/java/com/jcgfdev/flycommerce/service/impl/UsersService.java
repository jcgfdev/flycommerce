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
import com.jcgfdev.flycommerce.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService implements IUsersService {
    //Utils
    private final IUsersMapper usersMapper;
    private final PasswordEncoder encoder;
    //Repositories
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    //Exceptions
    private static final String ROLENOTFOUNDEXCEPTION = "Rol no existe";
    private static final String USERNOTFOUNDEXCEPTION = "Usuario no existe";
    private static final String USERALREADYEXISTS = "Usuario ya registrado :)!!";

    @Override
    public UsersDTO findById(UUID id) {
        return usersMapper.toDto(userRepository.findById(id).orElseThrow(() -> new DataNotFoundException(USERNOTFOUNDEXCEPTION)));
    }

    @Override
    public UsersDTO saveUser(CreateUserRequest createUserRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(createUserRequest.getEmail()))) {
            log.debug("Error al crear usuario, ya existe");
            throw new AlreadyExistsException(USERALREADYEXISTS);
        }
        Users user = new Users(
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                createUserRequest.getEmail(),
                encoder.encode(createUserRequest.getPassword())
        );
        user.setRoles(resolveRoles(createUserRequest.getRole()));
        Users savedUser = userRepository.save(user);
        return usersMapper.toDto(savedUser);
    }

    @Override
    public UsersDTO findByEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException(USERNOTFOUNDEXCEPTION));
        return usersMapper.toDto(user);
    }

    private Set<Roles> resolveRoles(Set<String> strRoles) {
        Set<Roles> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(findRole(Role.USER));
        } else {
            for (String role : strRoles) {
                roles.add(switch (role) {
                    case "admin" -> findRole(Role.ADMIN);
                    case "sell" -> findRole(Role.SELLER);
                    default -> findRole(Role.USER);
                });
            }
        }
        return roles;
    }

    private Roles findRole(Role role) {
        return rolesRepository.findByRol(role)
                .orElseThrow(() -> new DataNotFoundException(ROLENOTFOUNDEXCEPTION));
    }
}
