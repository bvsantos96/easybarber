package com.teamsantos.easybarber.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTOs;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.SchedulesDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.services.UserService;
import com.teamsantos.easybarber.utils.Pair;

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
    public ResponseEntity<BaseResponseDTOs> create(@RequestBody ScheduleDTO obj, Principal principal,
            @RequestParam(required = false) Boolean forceSave,
            @RequestParam(required = false) Boolean replaceExisting) {
        try {
            if (forceSave == null) {
                forceSave = true;
            }

            if (replaceExisting == null) {
                replaceExisting = true;
            }
            Pair<List<Long>, String> result = schedulesService.create(obj, userService.getEmployee(principal),
                    forceSave,
                    replaceExisting);
            return ResponseEntity
                    .status(HttpStatus.CREATED).body(new BaseResponseDTOs(result.getFirst(), result.getSecond()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTOs(e.getMessage()));
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<BasePageDTO<SchedulesDTO>> getSchedules(@ModelAttribute ScheduleFilter filter,
            Pageable pageable) {
        try {
            BasePageDTO<SchedulesDTO> schedules = schedulesService.getSchedulesMerged(filter, pageable);
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @DeleteMapping("/schedule/{id}")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> disable(@PathVariable Long id, Principal principal) {
        try {
            schedulesService.disable(id, userService.getEmployee(principal));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/schedule/exception")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseListDTO<Long>> createException(@RequestBody ScheduleExceptionDTO exception,
            Principal principal) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseListDTO<Long>(
                    schedulesService.createException(exception, userService.getEmployee(principal))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseListDTO<>(e.getMessage()));
        }
    }
}
