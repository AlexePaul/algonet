package com.algonet.algonetapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "problems")
@Getter
@Setter
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @NonNull
    @Column(name = "time_limit", nullable = false)
    private Integer timeLimit;
    @NonNull
    @Column(name="memory_limit", nullable = false)
    private Integer memoryLimit;
    @NonNull
    @Column(nullable = false)
    private String title;
    @NonNull
    @Column(nullable = false)
    private String body;
    @NonNull
    @Column(nullable = false)
    private String restrictions;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
