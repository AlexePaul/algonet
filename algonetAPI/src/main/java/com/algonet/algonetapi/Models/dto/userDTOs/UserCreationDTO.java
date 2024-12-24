package com.algonet.algonetapi.Models.dto.userDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserCreationDTO {
    private String username;
    private String password;
    private String email;
}
