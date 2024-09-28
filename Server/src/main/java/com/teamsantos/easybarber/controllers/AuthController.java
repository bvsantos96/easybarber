package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.ResetPwdDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.services.MessagingService;
import com.teamsantos.easybarber.services.UserService;

@RestController
public class AuthController {
    private final UserService userService;
    private final MessagingService messagingService;
    @Autowired
    public AuthController(UserService userService, MessagingService messagingService) {
        this.userService = userService;
        this.messagingService = messagingService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCreateDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.loginUser(userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userDTO) {
        HttpStatus status = HttpStatus.CREATED;
        try {
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

    @GetMapping("/pwd/reset")
    public ResponseEntity<UserDTO> getUserLocations(@RequestBody ResetPwdDTO resetPwdDTO) {
        try {
            UserDTO userDTO = userService.getUserByMobileNr(resetPwdDTO.getPhoneNr());
            messagingService.verifyCode(resetPwdDTO.getPhoneNr(), resetPwdDTO.getConfirmationCode());
            userService.changeUserPwd(userDTO, resetPwdDTO.getNewPassword());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            UserDTO userDTO = new UserDTO();
            userDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(userDTO);
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
