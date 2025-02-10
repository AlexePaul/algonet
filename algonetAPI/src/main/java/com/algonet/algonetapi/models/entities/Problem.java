package com.algonet.algonetapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
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

    @JsonProperty("authorId")
    private Integer getAuthorId(){
        return author.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Problem problem = (Problem) o;
        return id.equals(problem.id) &&
                author.equals(problem.author) &&
                timeLimit.equals(problem.timeLimit) &&
                memoryLimit.equals(problem.memoryLimit) &&
                title.equals(problem.title) &&
                body.equals(problem.body) &&
                restrictions.equals(problem.restrictions) &&
                createdAt.equals(problem.createdAt);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
