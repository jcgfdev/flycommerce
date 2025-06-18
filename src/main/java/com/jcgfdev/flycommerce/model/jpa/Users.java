package com.jcgfdev.flycommerce.model.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @PrePersist
    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @NotBlank
    @Size(max = 120)
    @NonNull
    private String firstName;
    @NotBlank
    @Size(max = 120)
    @NonNull
    private String lastName;
    @NotBlank
    @Size(max = 50)
    @NonNull
    @Email
    private String email;
    @NotBlank
    @Size(max = 120)
    @NonNull
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();
    private Boolean locked = false;
    private Boolean enabled = false;
}
