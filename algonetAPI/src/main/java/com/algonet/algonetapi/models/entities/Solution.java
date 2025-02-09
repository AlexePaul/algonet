package com.algonet.algonetapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "solutions")
@Getter
@Setter
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @JsonIgnore
    private Problem problem;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @NonNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NonNull
    @Column(nullable = false, length = 20000)
    private String code;

    private Integer grade;

    @JsonProperty("problemId")
    private Integer getProblemId(){
        return problem.getId();
    }

    @JsonProperty("userId")
    private Integer getUserId(){
        return user.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return grade.equals(solution.grade) &&
                problem.equals(solution.problem) &&
                user.equals(solution.user) &&
                createdAt.equals(solution.createdAt) &&
                code.equals(solution.code);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
