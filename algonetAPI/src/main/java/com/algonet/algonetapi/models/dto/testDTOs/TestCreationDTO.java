package com.algonet.algonetapi.models.dto.testDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TestCreationDTO {
   @NonNull
   private String input;
   @NonNull
   private String output;
}
