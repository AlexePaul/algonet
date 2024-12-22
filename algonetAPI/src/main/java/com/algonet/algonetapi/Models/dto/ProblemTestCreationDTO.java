package com.algonet.algonetapi.Models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ProblemTestCreationDTO {
    private ProblemCreationDTO problem;
    private List<TestCreationDTO> tests;
}
