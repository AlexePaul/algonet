package com.algonet.algonetapi.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemRatingId implements Serializable {

    @NonNull
    @Column(name = "problem_id", nullable = false)
    private Integer problemId;

    @NonNull
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemRatingId that = (ProblemRatingId) o;
        return Objects.equals(problemId, that.problemId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemId, tagId);
    }
}
