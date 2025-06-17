package com.jcgfdev.flycommerce.dto;

import com.jcgfdev.flycommerce.security.model.Role;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private List<String> roles;
}
