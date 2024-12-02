package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.user.ResetPwdDTO;
import com.teamsantos.easybarber.DTO.user.UserCreateDTO;
import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.services.MessagingService;
import com.teamsantos.easybarber.services.UserService;

@RestController
public class AuthController {
    @Value("${teamsantos.istest}")
    private boolean isTestContext;

    private final UserService userService;
    private final MessagingService messagingService;

    @Autowired
    public AuthController(UserService userService, MessagingService messagingService) {
        this.userService = userService;
        this.messagingService = messagingService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCreateDTO userDTO,
            @RequestParam(required = false) Boolean employeeOnly) {
        try {
            userDTO.setMobile(userDTO.getMobile().replace(" ", ""));
            if (employeeOnly == null) {
                employeeOnly = false;
            }
            return ResponseEntity.ok(userService.loginUser(userDTO, employeeOnly));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userDTO) {
        HttpStatus status = HttpStatus.CREATED;
        try {
            userDTO.setMobile(userDTO.getMobile().replace(" ", ""));
            if (!isTestContext) {
                messagingService.verifyCode(userDTO.getMobileInformation(),
                        userDTO.getConfirmationCode());
            }
            return ResponseEntity.status(status).body(userService.createUser(userDTO));
        } catch (Exception e) {
            UserDTO response = new UserDTO();
            response.setResponseMessage(e.getMessage());
            if (e instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (e instanceof UserAlreadyExistsException) {
                status = HttpStatus.FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(status).body(response);
        }
    }

    @PutMapping("/pwd/reset")
    public ResponseEntity<BaseResponseDTO> resetUserPwd(@RequestBody ResetPwdDTO resetPwdDTO) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            UserDTO userDTO = userService.getUserByMobileNr(resetPwdDTO.getPhoneNr());
            messagingService.verifyCode(resetPwdDTO.getPhoneNr(),
                    resetPwdDTO.getConfirmationCode());
            userService.changeUserPwd(userDTO, resetPwdDTO.getNewPassword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Profile("test")
    @PostMapping("/registerAdmin")
    public ResponseEntity<UserDTO> createAdmin(@RequestBody UserCreateDTO userDTO) {
        HttpStatus status = HttpStatus.CREATED;
        try {
            return ResponseEntity.status(status).body(userService.createAdmin(userDTO));
        } catch (Exception e) {
            UserDTO response = new UserDTO();
            response.setResponseMessage(e.getMessage());
            if (e instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (e instanceof UserAlreadyExistsException) {
                status = HttpStatus.FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(status).body(response);
        }
    }
}
