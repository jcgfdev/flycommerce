package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.exception.DataNotFoundException;
import com.jcgfdev.flycommerce.payload.request.LoginRequest;
import com.jcgfdev.flycommerce.payload.response.LoginResponse;
import com.jcgfdev.flycommerce.security.jwt.JwtProvider;
import com.jcgfdev.flycommerce.security.model.Role;
import com.jcgfdev.flycommerce.service.IAuthService;
import com.jcgfdev.flycommerce.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {
    //Seguridad
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    //servicios
    private final IUsersService usersService;
    //mensajes
    private static final String USERDONTEXISTS = "usuario no existe";

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            log.info("Inicio de creacion de sesion");
            //Verificar existencia de usuario
            UsersDTO user = Optional.ofNullable(usersService.findByEmail(loginRequest.getUser()))
                    .orElseThrow(() -> {
                        log.error("Intento de creacion de usuario: {}", USERDONTEXISTS);
                        return new DataNotFoundException(USERDONTEXISTS);
                    });
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUser(), loginRequest.getPassword())
            );
            // Establecer el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var roles = user.getRoles().stream()
                    .map(Role::valueOf)
                    .toList();
            String token = jwtProvider.generateToken(user.getEmail(), roles);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUser(user.getEmail());
            loginResponse.setFirstName(user.getFirstName());
            loginResponse.setLastName(user.getLastName());
            loginResponse.setToken(token);
            log.info("contexto de seguridad creado correctamente, bienvenido mr.{}", user.getLastName());
            return loginResponse;
        } catch (Exception e) {
            log.error("Error durante login: {}", e.getMessage(), e);
            throw new SecurityException("Error durante login: " + e.getMessage());
        }
    }
}