package com.jcgfdev.flycommerce.docs;

import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.exception.responses.ErrorResponseDTO;
import com.jcgfdev.flycommerce.payload.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.ResponseEntity;

import static com.jcgfdev.flycommerce.docs.constants.OpenApiExamples.*;

public interface UsersApiDocs {

    @RequestBody(
            description = "Datos necesarios para crear un usuario",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CreateUserRequest.class),
                    examples = @ExampleObject(value = EXAMPLE_USER_CREATED_REQUEST)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_USER_CREATED))
            ),
            @ApiResponse(responseCode = "400", description = "Error de validación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_VALIDATION_ERROR))
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_UNAUTHORIZED_ERROR))
            ),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_FORBIDDEN_ERROR))
            ),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_ROLE_NOT_FOUND))
            ),
            @ApiResponse(responseCode = "410", description = "Usuario ya existe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = EXAMPLE_USER_EXISTS))
            )
    })
    ResponseEntity<UsersDTO> saveUser(CreateUserRequest createUserRequest);
}
