package com.jcgfdev.flycommerce.docs.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiExamples {

    public static final String EXAMPLE_USER_CREATED = """
                {
                  "id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
                  "email": "usuario@example.com",
                  "firstName": "Juan",
                  "lastName": "Pérez",
                  "status": true
                }
            """;

    public static final String EXAMPLE_USER_CREATED_REQUEST = """
                {
                  "firstName": "Juan",
                  "lastName": "Pérez",
                  "email": "juan.perez@example.com",
                  "role": ["user", "sell"],
                  "password": "Password123!"
                }
            """;


    public static final String EXAMPLE_VALIDATION_ERROR = """
                {
                  "timestamp": "2025-04-25T13:30:00",
                  "status": 400,
                  "error": "Bad Request",
                  "message": "Errores de validación: {email=Debe ser un correo válido, firstName=No puede ser vacío}",
                  "path": "/registration/saveUser"
                }
            """;

    public static final String EXAMPLE_UNAUTHORIZED_ERROR = """
                {
                  "timestamp": "2025-04-25T14:00:00",
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "No se encontró autenticación en la solicitud",
                  "path": "/registration/saveUser"
                }
            """;

    public static final String EXAMPLE_FORBIDDEN_ERROR = """
                {
                  "timestamp": "2025-04-25T14:00:00",
                  "status": 403,
                  "error": "Forbidden",
                  "message": "Acceso denegado",
                  "path": "/registration/saveUser"
                }
            """;

    public static final String EXAMPLE_ROLE_NOT_FOUND = """
                {
                  "timestamp": "2025-04-25T13:30:00",
                  "status": 404,
                  "error": "Not Found",
                  "message": "El rol solicitado no existe",
                  "path": "/registration/saveUser"
                }
            """;

    public static final String EXAMPLE_USER_EXISTS = """
                {
                  "timestamp": "2025-04-25T13:30:00",
                  "status": 410,
                  "error": "Gone",
                  "message": "El usuario ya se encuentra registrado",
                  "path": "/registration/saveUser"
                }
            """;
}
