package com.teamsantos.easybarber.DTO;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsersDTO extends BaseResponseDTO {
    private List<UserDTO> users;
}
