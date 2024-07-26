package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
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

    @PostMapping("/schedule")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_EMPLOYEE_OBJECT)
    public ResponseEntity<String[]> create(@RequestBody ScheduleDTO obj, Principal principal,
            @PathVariable(required = false) Boolean forceSave) {
        try {
            if (forceSave == null) {
                forceSave = true;
            }
            return ResponseEntity.ok(schedulesService.create(obj, userService.getEmployee(principal), forceSave));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/schedule/test3")
    public ResponseEntity<String> test3() {
        return ResponseEntity.ok("Test");
    }

    @PostMapping("/schedule/test2")
    public ResponseEntity<String> test2(@RequestBody ScheduleDTO obj, Principal principal) {
        return ResponseEntity.ok("Test");
    }

    @PostMapping("/schedule/test")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_EMPLOYEE_OBJECT)
    public ResponseEntity<String> test(@RequestBody ScheduleDTO obj, Principal principal) {
        return ResponseEntity.ok("Test");
    }

    @GetMapping("/schedules")
    public ResponseEntity<BasePageDTO<ScheduleDTO>> getSchedules(@ModelAttribute ScheduleFilter filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok(schedulesService.getSchedules(filter, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }
}
