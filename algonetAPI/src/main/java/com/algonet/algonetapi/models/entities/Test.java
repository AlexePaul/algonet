package com.algonet.algonetapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "tests")
@Getter
@Setter
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @JsonIgnore
    private Problem problem;

    private String input;

    @NonNull
    @Column(nullable = false)
    private String output;

    @JsonProperty("problemId")
    private Integer getProblemId(){
        return problem.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Test test = (Test) o;
        return id.equals(test.id) &&
                problem.equals(test.problem) &&
                input.equals(test.input) &&
                output.equals(test.output);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
