package com.algonet.algonetapi.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_ratings")
@Getter
@Setter
public class UserRating {

    @EmbeddedId
    private UserRatingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    private Integer rating;
}
