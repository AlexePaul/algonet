package com.algonet.algonetapi.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    @Column(nullable = false, updatable = false, unique = true)
    private String username;
    @NonNull
    @Column(nullable = false)
    private String password;
    @Email
    private String email;
    private String role;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return  id.equals(user.id) &&
                username.equals(user.username) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                role.equals(user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, role);
    }
}
