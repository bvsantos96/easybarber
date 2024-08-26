package com.teamsantos.easybarber.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.EmployeeCreateDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.images.EmployeeImage;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.EmployeeService;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.services.ServiceService;
import com.teamsantos.easybarber.services.UserService;

@Controller
@RequestMapping("/employee")
public class EmployeeController extends ImageController<Employee, EmployeeImage> {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final ServiceService serviceService;
    private final SchedulesService schedulesService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, UserService userService, ServiceService serviceService,
            SchedulesService schedulesService) {
        super(employeeService);
        this.employeeService = employeeService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.schedulesService = schedulesService;
    }

    @PostMapping
    public ResponseEntity<BaseResponseDTO> createEmployee(@RequestBody EmployeeCreateDTO employee,
            Principal principal) {
        HttpStatus status = HttpStatus.CREATED;
        try {
            if (principal != null && !userService.userChangePermissions(principal, employee.getMobileInformation()))
                return ResponseEntity.badRequest().body(new BaseResponseDTO("You are not allowed to create this user"));
            userService.createUser(employee, true);
            return ResponseEntity.status(status).body(new BaseResponseDTO("Employee created successfully"));
        } catch (Exception e) {
            BaseResponseDTO response = new BaseResponseDTO();
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

    @DeleteMapping
    public ResponseEntity<BaseResponseDTO> deleteEmployee(Principal principal) {
        try {
            employeeService.deleteEmployee(principal);
            return ResponseEntity.ok(new BaseResponseDTO("Employee deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/service")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> createService(@RequestBody ServiceDTO service, Principal principal) {
        try {
            serviceService.createService(service, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDTO("Service created successfully"));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(new BaseResponseDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PutMapping("/service")
    @PreAuthorize(PrePermissionEvaluator.SERVICE_OWNER_OBJECT)
    public ResponseEntity<BaseResponseDTO> updateService(@RequestBody ServiceDTO service) {
        try {
            serviceService.updateService(service);
            return ResponseEntity.ok(new BaseResponseDTO("Service updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/services")
    public ResponseEntity<BasePageDTO<ServiceDTO>> getServices(Principal principal, Pageable pageable) {
        try {
            return ResponseEntity.ok(new BasePageDTO<>(serviceService.getServices(principal, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<BasePageDTO<ServiceDTO>> getServices(@PathVariable("id") Long id, Pageable pageable) {
        try {
            return ResponseEntity.ok(new BasePageDTO<>(serviceService.getServices(id, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/establishments")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> getEstablishments(Principal principal,
            @RequestParam(defaultValue = "false") boolean owned, Pageable pageable) {
        try {
            return ResponseEntity
                    .ok(new BasePageDTO<>(userService.getEstablishments(principal, owned, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/{id}/establishments")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> getEstablishments(@PathVariable("id") Long id,
            @RequestParam(defaultValue = "false") boolean owned, Pageable pageable) {
        try {
            return ResponseEntity
                    .ok(new BasePageDTO<>(userService.getEstablishments(id, owned, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @Override
    @PostMapping("/{entityId}/images")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> addImages(@PathVariable("entityId") Long entityId,
            @RequestBody List<ImageDTO> images, Principal principal) {
        try {
            if (principal != null && userService.userChangePermissions(principal,
                    userService.getUser(principal).getMobileInformation()))
                return _addImages(entityId, images);
            else
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new BaseResponseDTO("User not allowed to update this entity"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/schedule/exception")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BasePageDTO<ScheduleExceptionDTO>> getExceptions(
            @RequestParam(required = false) Boolean active,
            Pageable pageable, Principal principal) {
        try {
            if (active == null) {
                active = true;
            }
            ScheduleFilter filter = new ScheduleFilter();
            filter.setEmployeeId(employeeService.getEmployee(principal).getId());
            if (active) {
                filter.setActive(active);
            }
            return ResponseEntity.ok(schedulesService.getExceptions(filter, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }
}
