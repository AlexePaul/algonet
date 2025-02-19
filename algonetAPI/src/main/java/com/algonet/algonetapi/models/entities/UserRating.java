package com.algonet.algonetapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", nullable = false)
    @JsonIgnore
    private Tag tag;

    private Integer rating;

    @JsonProperty("userId")
    private Integer getUserId(){
        return user.getId();
    }

    @JsonProperty("tagId")
    private Integer getTagId(){
        return tag.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRating userRating = (UserRating) o;

        return rating.equals(userRating.rating) &&
                user.equals(userRating.user) &&
                tag.equals(userRating.tag);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
