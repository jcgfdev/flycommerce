package com.jcgfdev.flycommerce.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String user;
    private String firstName;
    private String lastName;
    private String token;
}
