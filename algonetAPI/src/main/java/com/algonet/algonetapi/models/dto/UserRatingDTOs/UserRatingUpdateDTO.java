package com.algonet.algonetapi.models.dto.UserRatingDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRatingUpdateDTO {
    private Integer tagId;
    private Integer rating;
}
