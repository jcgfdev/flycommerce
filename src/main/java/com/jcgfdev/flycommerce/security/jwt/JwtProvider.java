package com.jcgfdev.flycommerce.security.jwt;

import com.jcgfdev.flycommerce.dto.UserDetailsModel;
import com.jcgfdev.flycommerce.security.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase responsable de generar y validar JWT.
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {

    // Inyectamos las propiedades del token desde application-*.yml
    private final JwtProperties jwtProperties;

    // Clave secreta usada para firmar el token
    private Key secretKey;

    @PostConstruct
    public void init() {
        // Inicializa la clave secreta con el valor desde las properties
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Genera un token JWT con múltiples roles.
     *
     * @param username el nombre de usuario (único)
     * @param roles    lista de roles del usuario
     * @return token JWT firmado
     */
    public String generateToken(String username, List<Role> roles) {
        // Convertimos los roles a una lista de Strings (ej: ["ADMINISTRADOR", "USUARIO"])
        List<String> roleNames = roles.stream()
                .map(Role::name)
                .toList();

        return Jwts.builder()
                .setSubject(username) // quién es el usuario
                .claim("roles", roleNames) // metemos los roles en el token
                .setIssuedAt(new Date()) // fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration())) // expiración
                .signWith(secretKey, SignatureAlgorithm.HS256) // firmamos el token
                .compact();
    }

    /**
     * Extrae el username del token.
     */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extrae la lista de roles del token como lista de Strings.
     */
    public List<String> getRolesFromToken(String token) {
        return parseClaims(token).get("roles", List.class);
    }

    /**
     * Verifica si el token es válido y no está expirado.
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parsea el token y extrae los claims.
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
