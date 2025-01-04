package com.algonet.algonetapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "problem_ratings")
@Getter
@Setter
public class ProblemRating {

    @EmbeddedId
    private ProblemRatingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("problemId") // Maps the "problemId" part of the composite key
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId") // Maps the "tagId" part of the composite key
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    private Integer rating;
}
