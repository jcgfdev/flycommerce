package com.jcgfdev.flycommerce.payload.request;

import com.jcgfdev.flycommerce.security.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank(message = "Debe ingresar un nombre de usuario")
    String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{10,}$",
            message = "La clave debe tener al menos 10 caracteres, incluir una mayuscula, un numero y un caracter especial"
    )
    String password;
    List<Role> roles;
}
