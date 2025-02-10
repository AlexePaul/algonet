package com.algonet.algonetapi.models.dto.problemDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProblemCreationDTO {
    private String title;
    private String body;
    private String restrictions;
    private Integer timeLimit;
    private Integer memoryLimit;
}
