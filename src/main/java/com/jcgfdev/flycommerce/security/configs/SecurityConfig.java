package com.jcgfdev.flycommerce.security.configs;

import com.jcgfdev.flycommerce.security.handler.CustomAuthEntryPoint;
import com.jcgfdev.flycommerce.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthEntryPoint customAuthEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {
                })
                // Deshabilitamos CSRF porque:
                // - Esta aplicación es stateless (no maneja sesiones)
                // - Utiliza autenticación JWT vía headers Authorization
                // - No se usa almacenamiento en cookies ni formularios HTML que justifiquen CSRF tokens
                // - Todos los endpoints protegidos requieren token válido
                .csrf(csrf -> csrf.disable())
                //Configuración de endpoints públicos o protegidos
                .authorizeHttpRequests(auth -> auth
                        // Publicos
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/auth/recover-password",
                                "/registration/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/login/**",
                                "/error"
                        )
                        .permitAll()
                        // Privados definidos
                        /*.requestMatchers("").authenticated()*/
                        // Todos los demás requieren autenticación
                        .anyRequest().authenticated()
                )
                //Evita que Spring cree sesiones, porque usamos JWT
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //Envia mensaje de excepcion
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint)
                )
                //Agrega el filtro JWT antes del filtro de autenticación por username
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //Expone el AuthenticationManager como Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
