package com.teamsantos.easybarber.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.schedule.SchedulesDTO;
import com.teamsantos.easybarber.DTO.schedule.TimeSlotsDTO;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.utils.Pair;

@RestController
public class SchedulesController {
    private final SchedulesService schedulesService;

    public SchedulesController(SchedulesService schedulesService) {
        this.schedulesService = schedulesService;
    }

    @PostMapping("/schedule")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_EMPLOYEE_OBJECT)
    public ResponseEntity<BaseResponseDTO> create(@RequestBody ScheduleDTO obj,
            @RequestParam(required = false) Boolean forceSave,
            @RequestParam(required = false) Boolean replaceExisting) {
        try {
            if (forceSave == null) {
                forceSave = true;
            }

            if (replaceExisting == null) {
                replaceExisting = true;
            }
            Pair<List<Long>, String> result = schedulesService.create(obj, UserContext.getEmployeeId(),
                    forceSave,
                    replaceExisting);
            return ResponseEntity
                    .status(HttpStatus.CREATED).body(new BaseResponseDTO(result.getFirst(), result.getSecond()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<BasePageDTO<SchedulesDTO>> listSchedules(@ModelAttribute ScheduleFilter filter,
            Pageable pageable) {
        try {
            BasePageDTO<SchedulesDTO> schedules = schedulesService.getSchedulesMerged(filter, pageable);
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/schedules/day")
    public ResponseEntity<TimeSlotsDTO> listSchedulesByDay(@ModelAttribute ScheduleFilter filter) {
        try {
            if (filter.getFrom() == null) {
                throw new IllegalArgumentException("From date needs to be provided");
            }
            filter.setTo(filter.getFrom().plusDays(1));
            return ResponseEntity.ok(schedulesService.getSchedulesByDay(filter));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TimeSlotsDTO(e.getMessage()));
        }
    }

    @GetMapping("/schedules/availabledays/year/{year}/month/{month}")
    public ResponseEntity<List<String>> listDaysByAvailability(
            @PathVariable Integer year,
            @PathVariable Integer month,
            @RequestParam(required = true) long establishmentId,
            @RequestParam(required = true) long serviceId,
            @RequestParam(required = true) boolean available,
            @RequestParam(required = false) Long employeeId) {
        try {
            ScheduleFilter filter = new ScheduleFilter();
            filter.setFrom(LocalDate.of(year, month, 1));
            filter.setTo(filter.getFrom().plusMonths(1));
            filter.setActive(true);
            filter.setEstablishmentId(establishmentId);
            filter.setServiceId(serviceId);
            filter.setEmployeeId(employeeId);
            return ResponseEntity.ok(schedulesService.getDaysByAvailability(filter, available));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @DeleteMapping("/schedule/{id}")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> disable(@PathVariable Long id) {
        try {
            schedulesService.disable(id, UserContext.getEmployeeId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/schedule/exception")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> createException(@RequestBody ScheduleExceptionDTO exception) {
        try {
            Set<Long> ids = schedulesService.createException(exception, UserContext.getEmployeeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDTO(ids));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }
}
