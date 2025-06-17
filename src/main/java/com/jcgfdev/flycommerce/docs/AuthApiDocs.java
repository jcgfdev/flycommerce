package com.jcgfdev.flycommerce.docs;

import com.jcgfdev.flycommerce.exception.responses.ErrorResponseDTO;
import com.jcgfdev.flycommerce.payload.request.AuthRequest;
import com.jcgfdev.flycommerce.payload.request.RegisterUserRequest;
import com.jcgfdev.flycommerce.payload.response.AuthResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.ResponseEntity;

import static com.jcgfdev.flycommerce.docs.constants.OpenApiExamples.*;

public interface AuthApiDocs {

    @RequestBody(
            description = "Datos necesarios para registrar un nuevo usuario",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterUserRequest.class),
                    examples = @ExampleObject(value = EXAMPLE_REGISTER_USER_REQUEST)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_AUTH_RESPONSE))
            ),
            @ApiResponse(responseCode = "400", description = "Error de validación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_VALIDATION_ERROR))
            ),
            @ApiResponse(responseCode = "410", description = "Usuario ya existe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_USER_EXISTS))
            )
    })
    ResponseEntity<AuthResponse> register(RegisterUserRequest request);

    @RequestBody(
            description = "Credenciales de acceso",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthRequest.class),
                    examples = @ExampleObject(value = EXAMPLE_AUTH_REQUEST)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_AUTH_RESPONSE))
            ),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_UNAUTHORIZED_ERROR))
            )
    })
    ResponseEntity<AuthResponse> login(AuthRequest request);
}
