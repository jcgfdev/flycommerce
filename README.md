# flycommerce

`flycommerce` es un microservicio desarrollado en **Java 21** con **Spring Boot 3.4.4**, diseñado para la gestión de pedidos en una plataforma e-commerce. Este servicio implementa una arquitectura híbrida orientada a eventos asíncronos y gestión de datos en bases relacionales y no relacionales.

---

## Características principales

### Gestión de pedidos
- Persistencia de usuarios e inicio de sesión en **MySQL**.
- Gestión de pedidos y órdenes en **MongoDB**.
- Emisión y consumo de eventos de negocio mediante **Kafka**.

### Eventos asíncronos
- Productores y consumidores con **Kafka** para procesar pagos, actualizar inventario y orquestar el cumplimiento de pedidos.

### Herramientas para desarrollo
- Documentación de APIs con **Swagger / OpenAPI**.
- Reportes de calidad y análisis estático con **SonarQube**.
- Scripts de automatización con **Makefile**.

---

## Requisitos

| Herramienta        | Versión mínima |
|--------------------|----------------|
| Java               | 21             |
| Maven              | 3.9+           |
| Docker + Compose   | 20+            |
| SonarQube          | (opcional para análisis de calidad) |

---

## Ejecución local

> **Importante:** debes tener un archivo `.env.local` con las variables de entorno necesarias.

### Comandos equivalentes con y sin `make`

| Acción                            | Comando `make`       | Alternativa sin `make`                                  |
|----------------------------------|-----------------------|----------------------------------------------------------|
| Levantar stack DB + Kafka        | `make db-up`          | `docker compose -f Docker/docker-compose.yml --env-file .env.local up -d` |
| Apagar stack DB + Kafka          | `make db-down`        | `docker compose -f Docker/docker-compose.yml --env-file .env.local down` |
| Levantar SonarQube               | `make sonar-up`       | `docker compose -f Docker/docker-compose.sonar.yml --env-file .env.local up -d` |
| Apagar SonarQube                 | `make sonar-down`     | `docker compose -f Docker/docker-compose.sonar.yml --env-file .env.local down` |
| Compilar + ejecutar tests        | `make test`           | `./mvnw test` (o `mvnw.cmd test` en Windows)             |
| Generar cobertura JaCoCo         | `make coverage`       | `./mvnw verify`                                          |
| Análisis SonarQube               | `make sonar`          | Ver sección avanzada más abajo con parámetros específicos |

---

## Comandos `make` disponibles

| Comando               | Descripción                                     |
|-----------------------|-------------------------------------------------|
| `make build`          | Compila el proyecto con pruebas incluidas       |
| `make test`           | Ejecuta las pruebas unitarias                   |
| `make coverage`       | Genera reporte de cobertura con JaCoCo          |
| `make deploy`         | Publica en GitHub Packages                      |
| `make sonar`          | Lanza análisis de calidad con SonarQube         |
| `make sonar-up`       | Levanta contenedor de SonarQube con Docker      |
| `make sonar-down`     | Apaga contenedor de SonarQube                   |
| `make db-up`          | Levanta base de datos + MongoDB + Kafka         |
| `make db-down`        | Apaga base de datos + MongoDB + Kafka           |
| `make network-create` | Crea red `fly-net` si no existe                 |
| `make logs`           | Muestra los logs unificados de los contenedores |
| `make mysql`          | Accede al shell de MySQL                        |
| `make mongo`          | Accede al shell de MongoDB                      |
| `make kafka-producer` | Abre consola producer Kafka                     |
| `make kafka-consumer` | Abre consola consumer Kafka                     |
| `make help`           | Muestra esta ayuda                              |
| `make build-image`    | Construye la imagen del microservicio           |
| `make run-container`  | Ejecuta el contenedor del microservicio         |
| `make stop-container` | Detiene y elimina el contenedor                 |

---

## ¿No tienes `make` en Windows?

Puedes instalarlo fácilmente con alguna de estas opciones:

### Opción recomendada: Chocolatey
```bash
choco install make
```
> Si no tienes Chocolatey, puedes instalarlo desde: https://chocolatey.org/install

### Opción manual
1. Descarga `make.exe` desde [este enlace](https://ezg.it/make)
2. Copia el binario a una carpeta, por ejemplo `C:\Tools\make`
3. Agrega esa carpeta al `PATH` del sistema
4. Verifica con:
```bash
make --version
```

---

## Estructura del proyecto

```bash
Docker/
├── docker-compose.yml
├── docker-compose.sonar.yml
.env.local
Makefile
src/
└── main/
    └── java/com/jcgfdev/flycommerce/
        ├── config/
        ├── controller/
        ├── docs/
        ├── dto/
        ├── exception/
        ├── kafka/
        ├── mapper/
        ├── model/
        ├── payload/
        ├── repository/
        ├── runner/
        ├── security/
        ├── service/
└── test/
    └── java/com/jcgfdev/flycommerce/
        ├── service/impl/
        ├── mapper/
        └── ...
```

---

## Licencia

Este proyecto está licenciado bajo los términos definidos por **JCGFDev**. Uso interno para el microservicio de e-commerce **Flycommerce**.
