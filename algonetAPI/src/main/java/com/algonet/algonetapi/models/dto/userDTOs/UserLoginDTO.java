package com.algonet.algonetapi.models.dto.userDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserLoginDTO {
    private String username;
    private String password;
}
