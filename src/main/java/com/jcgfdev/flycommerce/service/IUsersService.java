package com.jcgfdev.flycommerce.service;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.payload.request.CreateUserRequest;

import java.util.UUID;

public interface IUsersService {
    UsersDTO findById(UUID id);
    UsersDTO saveUser(CreateUserRequest createUserRequest);
    UsersDTO findByEmail(String email);
}
