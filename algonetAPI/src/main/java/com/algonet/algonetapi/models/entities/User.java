package com.algonet.algonetapi.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    @Column(nullable = false, updatable = false)
    private String username;
    @NonNull
    @Column(nullable = false)
    private String password;
    @Email
    private String email;
    private String role;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
