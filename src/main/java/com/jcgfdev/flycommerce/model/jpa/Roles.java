package com.jcgfdev.flycommerce.model.jpa;

import com.jcgfdev.flycommerce.security.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Builder
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role rol;
}
