package com.jcgfdev.flycommerce.mapper;

import com.jcgfdev.flycommerce.dto.UserDTO;
import com.jcgfdev.flycommerce.model.User;

public interface IUserMapper {
    User dtoToEntity(UserDTO userDTO);
    UserDTO entityToDTO(User user);
}
