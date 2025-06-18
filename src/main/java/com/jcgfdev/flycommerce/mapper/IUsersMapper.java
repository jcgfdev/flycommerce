package com.jcgfdev.flycommerce.mapper;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.model.jpa.Users;

public interface IUsersMapper {
    UsersDTO toDto(Users users);
}
