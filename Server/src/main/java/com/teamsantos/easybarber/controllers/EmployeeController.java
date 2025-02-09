package com.teamsantos.easybarber.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.employee.EmployeeCreateDTO;
import com.teamsantos.easybarber.DTO.establishment.EstablishmentBaseDTO;
import com.teamsantos.easybarber.DTO.establishment.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.service.CreateServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.images.EmployeeImage;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.EmployeeService;
import com.teamsantos.easybarber.services.EstablishmentService;
import com.teamsantos.easybarber.services.MessagingService;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.services.ServiceService;
import com.teamsantos.easybarber.services.UserService;
import com.teamsantos.easybarber.services.UserTypeService;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Utils;

@RestController
@RequestMapping("/employee")
public class EmployeeController extends ImageController<Employee, EmployeeImage> {
    @Value("${teamsantos.istest}")
    private boolean isTestContext;

    private final EmployeeService employeeService;
    private final UserService userService;
    private final ServiceService serviceService;
    private final SchedulesService schedulesService;
    private final EstablishmentService establishmentService;
    private final MessagingService messagingService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, UserService userService, ServiceService serviceService,
            SchedulesService schedulesService, EstablishmentService establishmentService,
            MessagingService messagingService) {
        super(employeeService);
        this.employeeService = employeeService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.establishmentService = establishmentService;
        this.schedulesService = schedulesService;
        this.messagingService = messagingService;
    }

    @PostMapping
    public ResponseEntity<BaseResponseDTO> createEmployee(@RequestBody EmployeeCreateDTO employee,
            Principal principal) {
        HttpStatus status = HttpStatus.CREATED;
        try {
            if (!isTestContext) {
                messagingService.verifyCode(employee.getMobileInformation(),
                        employee.getConfirmationCode());
            }
            if (principal != null) {
                if (!userService.userChangePermissions(principal, employee.getMobileInformation()))
                    return ResponseEntity.badRequest()
                            .body(new BaseResponseDTO("You are not allowed to create this user"));
            }
            BaseResponseDTO response = new BaseResponseDTO();
            response.setResponseMessage("Employee created successfully");
            response.setId(userService.createUser(employee, true).getId());
            return ResponseEntity.status(status).body(response);
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
    public ResponseEntity<BaseResponseDTO> deleteEmployee() {
        try {
            employeeService.deleteEmployee();
            return ResponseEntity.ok(new BaseResponseDTO("Employee deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/service")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> createService(@RequestBody CreateServiceDTO service) {
        try {
            BaseResponseDTO response = new BaseResponseDTO();
            response.setId(serviceService.createService(service));
            response.setResponseMessage("Service created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public ResponseEntity<BasePageDTO<ServiceBaseDTO>> getServices(Pageable pageable) {
        try {
            ServiceFilter filter = new ServiceFilter();
            filter.setEmployeeId(UserContext.getEmployeeId());
            return ResponseEntity.ok(new BasePageDTO<>(serviceService.listServices(filter, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<BasePageDTO<ServiceBaseDTO>> getServices(@PathVariable("id") Long id, Pageable pageable) {
        try {
            ServiceFilter filter = new ServiceFilter();
            filter.setEmployeeId(id);
            return ResponseEntity.ok(new BasePageDTO<>(serviceService.listServices(filter, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/establishments")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> getEstablishments(
            @RequestParam(defaultValue = "false") boolean owned, Pageable pageable) {
        try {
            return ResponseEntity
                    .ok(new BasePageDTO<>(establishmentService
                            .getEstablishmentsByEmployeeId(UserContext.getEmployeeId(), owned, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/{id}/establishments")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> getEstablishments(@PathVariable("id") Long id,
            @RequestParam(defaultValue = "false") boolean owned, Pageable pageable) {
        try {
            return ResponseEntity
                    .ok(new BasePageDTO<>(establishmentService
                            .getEstablishmentsByEmployeeId(id, owned, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @Override
    public boolean canEdit(long entityId) {
        return UserContext.getCurrentUser().hasRole(UserTypeService.UserTypes.EMPLOYEE)
                && UserContext.getEmployeeId() == entityId;
    }

    @GetMapping("/schedule/exception")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BasePageDTO<ScheduleExceptionDTO>> getExceptions(
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        try {
            if (active == null) {
                active = true;
            }
            ScheduleFilter filter = new ScheduleFilter();
            filter.setEmployeeId(UserContext.getCurrentUser().getEmployeeId());
            if (active) {
                filter.setActive(active);
            }
            return ResponseEntity.ok(schedulesService.getExceptions(filter, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/schedules")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BasePageDTO<ScheduleDTO>> getSchedules(@RequestParam(required = false) Long establishmentId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        try {
            if (active == null) {
                active = true;
            }
            ScheduleFilter filter = new ScheduleFilter();
            if (establishmentId != null) {
                filter.setEstablishmentId(establishmentId);
            }
            filter.setEndHour(Utils.getEndOfDayTime());
            filter.setEmployeeId(UserContext.getEmployeeId());
            // filter.setActive(active);
            filter.setDayOfWeek(Set.of(DAY_OF_WEEK.values()));
            if (active) {
                filter.setActive(active);
            }
            return ResponseEntity.ok(schedulesService.getSchedules(filter, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @PostMapping("/schedule")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> create(@RequestBody ScheduleDTO obj,
            @RequestParam(required = false) Boolean forceSave,
            @RequestParam(required = false) Boolean replaceExisting) {
        try {
            if (forceSave == null) {
                forceSave = false;
            }

            if (replaceExisting == null) {
                replaceExisting = false;
            }

            obj.setEmployeeId(UserContext.getEmployeeId());

            Pair<List<Long>, String> result = schedulesService.create(obj, UserContext.getEmployeeId(),
                    forceSave,
                    replaceExisting);
            return ResponseEntity
                    .status(HttpStatus.CREATED).body(new BaseResponseDTO(result.getFirst(), result.getSecond()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @DeleteMapping("/schedule/{id}")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> deleteSchedule(@PathVariable("id") Long id) {
        try {
            schedulesService.deleteSchedule(id, UserContext.getEmployeeId());
            return ResponseEntity.ok(new BaseResponseDTO("Schedule deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/establishments/small")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseListDTO<EstablishmentBaseDTO>> getEstablishments() {
        try {
            return ResponseEntity.ok(new BaseListDTO<EstablishmentBaseDTO>(
                    employeeService.getEstablishments(UserContext.getEmployeeId())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
