package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.model.jpa.Roles;
import com.jcgfdev.flycommerce.repository.jpa.RolesRepository;
import com.jcgfdev.flycommerce.service.IRolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesService implements IRolesService {
    private final RolesRepository rolesRepository;

    @Override
    public Roles crearRol(Roles roles) {
        return rolesRepository.findByRol(roles.getRol())
                .orElseGet(() -> rolesRepository.save(roles));
    }
}
