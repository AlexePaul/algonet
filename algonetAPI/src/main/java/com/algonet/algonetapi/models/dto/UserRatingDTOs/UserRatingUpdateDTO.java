package com.algonet.algonetapi.models.dto.UserRatingDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRatingUpdateDTO {
    private Integer tagId;
    private Integer rating;
}
