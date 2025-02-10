package com.algonet.algonetapi.models.dto.testDTOs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestCreationDTO {
   @NonNull
   private String input;
   @NonNull
   private String output;
}
