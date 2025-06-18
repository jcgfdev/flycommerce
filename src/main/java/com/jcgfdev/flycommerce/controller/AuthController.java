package com.jcgfdev.flycommerce.controller;

import com.jcgfdev.flycommerce.payload.request.LoginRequest;
import com.jcgfdev.flycommerce.payload.response.LoginResponse;
import com.jcgfdev.flycommerce.service.IAuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Exitoso",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Error al procesar su solicitud, consulte al administrador", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor, consulte al admnistrador", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
}
