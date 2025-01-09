package com.algonet.algonetapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId") // Maps the "tagId" part of the composite key
    @JoinColumn(name = "tag_id", nullable = false)
    @JsonIgnore
    private Tag tag;

    private Integer rating;

    @JsonProperty("problemId")
    private Integer getProblemId(){
        return problem.getId();
    }

    @JsonProperty("tagId")
    private Integer getTagId(){
        return tag.getId();
    }
}
