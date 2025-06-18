package com.jcgfdev.flycommerce.runner;

import com.jcgfdev.flycommerce.model.jpa.Roles;
import com.jcgfdev.flycommerce.security.model.Role;
import com.jcgfdev.flycommerce.service.IRolesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
@Slf4j
public class RoleInitializer implements ApplicationRunner {
    private final IRolesService rolesService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("inicializando creacion de roles");
        rolesService.crearRol(Roles.builder().rol(Role.USER).build());
        rolesService.crearRol(Roles.builder().rol(Role.SELLER).build());
        rolesService.crearRol(Roles.builder().rol(Role.ADMIN).build());
        log.info("roles creados con exito!");
    }
}
