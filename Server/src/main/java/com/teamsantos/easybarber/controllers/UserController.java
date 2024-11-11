package com.teamsantos.easybarber.controllers;

import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.establishment.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.location.LocationDTO;
import com.teamsantos.easybarber.DTO.user.UserCreateDTO;
import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.LocationService;
import com.teamsantos.easybarber.services.UserService;
import com.teamsantos.easybarber.utils.GeometryUtils;

@RestController
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
    public ResponseEntity<String> updateUser(@RequestBody UserCreateDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @GetMapping("/locations")
    public ResponseEntity<BasePageDTO<LocationDTO>> getUserLocations(Pageable pageable) {
        BasePageDTO<LocationDTO> locations = new BasePageDTO<>();
        try {
            locations.setItems(locationService.getUserLocations(UserContext.getUserId(), pageable));
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            locations.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(locations);
        }
    }

    @PostMapping("/location")
    public ResponseEntity<Long> addUserLocation(@RequestBody LocationDTO locationDTO) {
        try {
            return ResponseEntity.ok(locationService.addLocation(locationDTO, UserContext.getUserId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }

    @GetMapping("/favorites/establishments")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> getFavoriteEstablishments(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            Pageable pageable) {
        try {
            Point location = null;
            if (latitude != null && longitude != null) {
                location = GeometryUtils.parseLocation(latitude, longitude);
            }
            return ResponseEntity
                    .ok(new BasePageDTO<>(
                            userService.getFavoriteEstablishments(UserContext.getUserId(), location, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/favorites/establishments/ids")
    public ResponseEntity<BaseListDTO<Long>> getFavoriteEstablishmentIds() {
        try {
            return ResponseEntity
                    .ok(new BaseListDTO<Long>(userService.getFavoriteEstablishmentsIds(UserContext.getUserId())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseListDTO<>());
        }
    }
}
