package com.jcgfdev.flycommerce.repository.jpa;

import com.jcgfdev.flycommerce.model.jpa.Roles;
import com.jcgfdev.flycommerce.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolesRepository extends JpaRepository<Roles, UUID> {
    Optional<Roles> findByRol(Role role);
}
