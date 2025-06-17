package com.jcgfdev.flycommerce.docs.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiExamples {

    public static final String EXAMPLE_REGISTER_USER_REQUEST = """
                {
                  "username": "juan123",
                  "password": "Password123!",
                  "roles": ["USER"]
                }
            """;

    public static final String EXAMPLE_AUTH_REQUEST = """
                {
                  "username": "juan123",
                  "password": "Password123!"
                }
            """;

    public static final String EXAMPLE_AUTH_RESPONSE = """
                {
                  "username": "juan123",
                  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                }
            """;

    public static final String EXAMPLE_VALIDATION_ERROR = """
                {
                  "timestamp": "2025-06-17T15:00:00",
                  "status": 400,
                  "error": "Bad Request",
                  "message": "Errores de validacion: {username=No puede ser vacio}",
                  "path": "/auth/register"
                }
            """;

    public static final String EXAMPLE_USER_EXISTS = """
                {
                  "timestamp": "2025-06-17T15:00:00",
                  "status": 410,
                  "error": "Gone",
                  "message": "El usuario ya existe",
                  "path": "/auth/register"
                }
            """;

    public static final String EXAMPLE_UNAUTHORIZED_ERROR = """
                {
                  "timestamp": "2025-06-17T15:00:00",
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "Credenciales invalidas",
                  "path": "/auth/login"
                }
            """;
}
