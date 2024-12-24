package com.algonet.algonetapi.Models.dto.problemDTOs;

import com.algonet.algonetapi.Models.dto.testDTOs.TestCreationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ProblemTestCreationDTO {
    private ProblemCreationDTO problem;
    private List<TestCreationDTO> tests;
}
