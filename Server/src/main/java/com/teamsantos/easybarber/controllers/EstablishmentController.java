package com.teamsantos.easybarber.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.teamsantos.easybarber.DTO.NameIdImagePriceDTO;
import com.teamsantos.easybarber.DTO.employee.EmployeeDTO;
import com.teamsantos.easybarber.DTO.employee.EmployeeListDTO;
import com.teamsantos.easybarber.DTO.establishment.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.establishment.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.establishment.EstablishmentInformationDTO;
import com.teamsantos.easybarber.DTO.establishment.service.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.filters.EmployeeFilter;
import com.teamsantos.easybarber.DTO.filters.EstablishmentFilter;
import com.teamsantos.easybarber.DTO.filters.ProductFilter;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.product.ProductDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceListDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.images.EstablishmentImage;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.EstablishmentService;
import com.teamsantos.easybarber.services.ProductService;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.services.UserService;
import com.teamsantos.easybarber.utils.Utils;

@RestController
@RequestMapping("/establishment")
public class EstablishmentController extends ImageController<Establishment, EstablishmentImage> {
    private final SchedulesService schedulesService;
    private final EstablishmentService establishmentService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public EstablishmentController(EstablishmentService service, SchedulesService schedulesService,
            UserService userService, ProductService productService) {
        super(service);
        this.establishmentService = service;
        this.schedulesService = schedulesService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstablishmentDTO> getEstablishment(@PathVariable Long id) {
        EstablishmentDTO establishment = new EstablishmentDTO();
        try {
            return ResponseEntity.ok(establishmentService.getEstablishmentDTO(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(establishment);
        } catch (Exception e) {
            establishment.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishment);
        }
    }

    @PostMapping
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseEstablishmentDTO> createEstablishment(
            @RequestBody BaseEstablishmentDTO establishmentDTO) {
        try {
            establishmentService.create(establishmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(establishmentDTO);
        } catch (AlreadyExistsException e) {
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body(establishmentDTO);
        } catch (Exception e) {
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishmentDTO);
        }
    }

    // GET /establishment/list?page=0&size=15&sort=id,desc
    @GetMapping("/list")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> listEstablishments(
            @ModelAttribute EstablishmentFilter filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok(new BasePageDTO<>(establishmentService.list(filter, pageable)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @PostMapping("/{establishmentId}/employee/{employeeId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addEmployee(@PathVariable("establishmentId") Long establishmentId,
            @PathVariable Long employeeId) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.addEmployee(establishmentId, employeeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (UserAlreadyExistsException e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/{establishmentId}/employee/{employeeId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> removeEmployee(@PathVariable("establishmentId") Long establishmentId,
            @PathVariable Long employeeId) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.removeEmployee(establishmentId, employeeId);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<EstablishmentInformationDTO> getEmployeeInformation(
            @PathVariable("id") Long establishmentId) {
        EstablishmentInformationDTO response = new EstablishmentInformationDTO();
        try {
            response = establishmentService.getInformation(establishmentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}/servicetypes")
    public ResponseEntity<BaseListDTO<Long>> listServicesTypes(@PathVariable Long id) {
        BaseListDTO<Long> response = new BaseListDTO<>();
        try {
            response.setItems(establishmentService.listServicesTypes(id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}/services/list")
    public ResponseEntity<BaseListDTO<ServiceListDTO>> listServices(@PathVariable Long id,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) Long establishmentStaffId) {
        BaseListDTO<ServiceListDTO> response = new BaseListDTO<>();
        try {
            response.setItems(establishmentService.listServices(id, date, establishmentStaffId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<BasePageDTO<ServiceDTO>> getServices(
            @PathVariable Long id, @RequestParam(required = false) LocalDateTime date, Pageable pageable) {
        BasePageDTO<ServiceDTO> listDTO = new BasePageDTO<>();
        try {
            listDTO.setItems(establishmentService.getServices(id, date, pageable));
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
        }
    }

    @PostMapping("/{establishmentId}/service")
    @PreAuthorize(PrePermissionEvaluator.SERVICE_OWNER_OBJECT_SERVICE_ID)
    public ResponseEntity<BaseResponseDTO> addService(@PathVariable Long establishmentId,
            @RequestBody CreateEstablishmentServiceDTO service) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            responseDTO.setId(establishmentService.addService(establishmentId, service));
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (UnsupportedOperationException e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
        } catch (AlreadyExistsException e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping("/{establishmentId}/service")
    @PreAuthorize(PrePermissionEvaluator.SERVICE_OWNER_OBJECT_SERVICE_ID)
    public ResponseEntity<BaseResponseDTO> updateService(@PathVariable Long establishmentId,
            CreateEstablishmentServiceDTO service) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.updateService(establishmentId, service);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/{establishmentId}/service/{establishmentServiceId}/employee")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addEmployeeToService(@PathVariable Long establishmentId,
            @PathVariable Long establishmentServiceId, @RequestBody Set<Long> employees) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.addEmployeesToService(establishmentId, establishmentServiceId, employees);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/{establishmentId}/service/{establishmentServiceId}/employee/{establishmentStaffId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> removeEmployeeFromService(@PathVariable Long establishmentId,
            @PathVariable Long establishmentServiceId, @PathVariable Long establishmentStaffId) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.removeEmployeeFromService(establishmentId, establishmentServiceId,
                    establishmentStaffId);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/{establishmentId}/service/{serviceId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> removeService(@PathVariable Long establishmentId,
            @PathVariable Long serviceId) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.removeService(establishmentId, serviceId);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * @param establishmentId
     * @param establishmentServiceId
     * @return List of employees of the establishment service, note that the id
     *         returned is the establishmentStaffId and not the employee id.
     */
    @GetMapping("/{establishmentId}/service/{establishmentServiceId}/employees")
    public ResponseEntity<BaseListDTO<NameIdImagePriceDTO>> listEmployeesOfEstablishmentService(
            @PathVariable Long establishmentId, @PathVariable Long establishmentServiceId,
            @RequestParam(required = false) LocalDateTime date) {
        BaseListDTO<NameIdImagePriceDTO> response = new BaseListDTO<>();
        try {
            response.setItems(
                    establishmentService.listEmployeesOfEstablishmentService(establishmentId, establishmentServiceId,
                            date));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{establishmentId}/employees/list")
    public ResponseEntity<BasePageDTO<EmployeeListDTO>> establishmentEmployeeList(@PathVariable long establishmentId,
            @ModelAttribute EmployeeFilter filter, Pageable pageable) {
        BasePageDTO<EmployeeListDTO> response = new BasePageDTO<>();
        try {
            response.setItems(
                    establishmentService.getListEmployees(establishmentId, filter, LocalDate.now(), pageable));
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            response.setResponseMessage(String.format("Establishment with id %d not found", establishmentId));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            response.setResponseMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // This is required to avoid missing USERCONTEXT because of public method 
    @GetMapping("/{establishmentId}/staff")
    public ResponseEntity<BaseListDTO<EmployeeDTO>> listStaff(@PathVariable Long establishmentId,
            @RequestParam(defaultValue = "true") boolean onlyActive) {
        BaseListDTO<EmployeeDTO> response = new BaseListDTO<>();
        try {
            response.setItems(establishmentService.getEmployees(establishmentId, onlyActive));
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            response.setResponseMessage(String.format("Establishment with id %d not found", establishmentId));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            response.setResponseMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{establishmentId}/employees")
    public ResponseEntity<BaseListDTO<EmployeeDTO>> listEmployees(@PathVariable Long establishmentId,
            @RequestParam(defaultValue = "true") boolean onlyActive) {
        BaseListDTO<EmployeeDTO> response = new BaseListDTO<>();
        try {
            response.setItems(establishmentService.getEmployees(establishmentId, onlyActive));
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            response.setResponseMessage(String.format("Establishment with id %d not found", establishmentId));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            response.setResponseMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public boolean canEdit(long entityId) {
        return EstablishmentSecurityExpressionRoot._hasAdminPermission(establishmentService, entityId);
    }

    @GetMapping("/{establishmentId}/schedule")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BasePageDTO<ScheduleDTO>> getSchedules(@PathVariable("establishmentId") long establishmentId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        try {
            if (active == null) {
                active = true;
            }
            ScheduleFilter filter = new ScheduleFilter();
            filter.setEstablishmentId(establishmentId);
            filter.setEndHour(Utils.getEndOfDayTime());
            filter.setActive(active);
            filter.setDayOfWeek(Set.of(DAY_OF_WEEK.values()));
            if (active) {
                filter.setActive(active);
            }
            return ResponseEntity.ok(schedulesService.getSchedules(filter, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @PostMapping("/{establishmentId}/favorite")
    public ResponseEntity<BaseResponseDTO> favorite(@PathVariable Long establishmentId) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            userService.favorite(establishmentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{establishmentId}/favorite")
    public ResponseEntity<BaseResponseDTO> unfavorite(@PathVariable Long establishmentId) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            userService.unfavorite(establishmentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{establishmentId}/favorite")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long establishmentId) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            return ResponseEntity.ok(userService.isFavorite(establishmentId));
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/{establishmentId}/product")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addProduct(@PathVariable Long establishmentId,
            @RequestBody ProductDTO product) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            product.setEstablishmentId(establishmentId);
            response.setId(productService.create(product));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/{establishmentId}/product")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> updateProduct(@PathVariable Long establishmentId,
            @RequestBody ProductDTO product) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            product.setEstablishmentId(establishmentId);
            response.setId(productService.update(product));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{establishmentId}/product/{productId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> disableProduct(@PathVariable Long establishmentId,
            @PathVariable Long productId) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            productService.disableProduct(Set.of(productId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{establishmentId}/products")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_EMPLOYEE)
    public ResponseEntity<BasePageDTO<ProductDTO>> listProducts(@PathVariable Long establishmentId,
            @RequestBody ProductFilter filter, Pageable pageable) {
        BasePageDTO<ProductDTO> response = new BasePageDTO<>();
        try {
            if (filter == null) {
                filter = new ProductFilter();
            }

            filter.setEstablishmentId(establishmentId);
            response.setItems(productService.getProducts(filter, pageable));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{establishmentId}/schedules/valid")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> hasValidSchedule(@PathVariable Long establishmentId) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (schedulesService.hasValidSchedule(establishmentId)) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
