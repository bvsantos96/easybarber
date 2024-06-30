package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.LocationDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.services.LocationService;
import com.teamsantos.easybarber.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class UserController {
    private final UserService userService;
    private final LocationService locationService;

    @Autowired
    public UserController(UserService userService, LocationService locationService) {
        this.userService = userService;
        this.locationService = locationService;
    }

    @GetMapping("/users")
    public ResponseEntity<BasePageDTO<UserDTO>> getAllUsers(@RequestParam(required = false) String userType,
            Pageable pageable) {
        try {
            Page<UserDTO> users;
            if (userType != null) {
                users = userService.getAllUsersByType(userType, pageable);
            } else {
                users = userService.getAllUsers(pageable);
            }
            return ResponseEntity.ok(new BasePageDTO<>(users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@RequestBody UserCreateDTO userDTO, Principal principal) {
        try {
            userService.updateUser(userDTO, principal);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @GetMapping("/locations")
    public ResponseEntity<BasePageDTO<LocationDTO>> getUserLocations(Principal principal, Pageable pageable) {
        BasePageDTO<LocationDTO> locations = new BasePageDTO<>();
        try {
            User user = userService.getUser(principal);
            locations.setItems(locationService.getUserLocations(user, pageable));
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            locations.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(locations);
        }
    }

    @PostMapping("/location")
    public ResponseEntity<Long> addUserLocation(@RequestBody LocationDTO locationDTO, Principal principal) {
        try {
            User user = userService.getUser(principal);
            return ResponseEntity.ok(locationService.addLocation(locationDTO, user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }
}
