package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.services.UserService;

@Controller
public class SchedulesController {
    private final UserService userService;
    private final SchedulesService schedulesService;

    public SchedulesController(SchedulesService schedulesService, UserService userService) {
        this.schedulesService = schedulesService;
        this.userService = userService;
    }

    @PostMapping("/schedules")
    public ResponseEntity<Long> create(@RequestBody ScheduleDTO schedule, Principal principal) {
        try {
            return ResponseEntity.ok(schedulesService.create(schedule, userService.getEmployee(principal)).getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
