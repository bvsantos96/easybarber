package com.teamsantos.easybarber.DTO.user;

import java.util.List;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsersDTO extends BaseResponseDTO {
    private List<UserDTO> users;
}
