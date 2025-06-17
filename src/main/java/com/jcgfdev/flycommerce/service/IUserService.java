package com.jcgfdev.flycommerce.service;

import com.jcgfdev.flycommerce.dto.UserDTO;
import com.jcgfdev.flycommerce.model.User;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;

public interface IUserService {
    UserDTO register(RegisterUserRequest registerUserRequest);

    User findByUsername(String username);
}
