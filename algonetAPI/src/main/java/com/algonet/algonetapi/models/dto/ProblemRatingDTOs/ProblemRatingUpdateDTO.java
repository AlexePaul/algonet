package com.algonet.algonetapi.models.dto.ProblemRatingDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProblemRatingUpdateDTO {
    private Integer problemId;
    private Integer tagId;
    private Integer rating;
}
