package com.jcgfdev.flycommerce.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Size(max = 120)
    private String firstName;
    @NotBlank
    @Size(max = 120)
    private String lastName;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private Set<String> role;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}

