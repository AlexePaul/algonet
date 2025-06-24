package com.algonet.algonetapi.models.dto.userProfileDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {
    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    private String website;
}
