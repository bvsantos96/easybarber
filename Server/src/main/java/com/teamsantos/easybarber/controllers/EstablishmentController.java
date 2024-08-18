package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.*;
import com.teamsantos.easybarber.DTO.filters.EstablishmentFilter;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.images.EstablishmentImage;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.EstablishmentService;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/establishment")
public class EstablishmentController extends ImageController<Establishment, EstablishmentImage> {

    private final SchedulesService schedulesService;
    private final EstablishmentService establishmentService;

    @Autowired
    public EstablishmentController(EstablishmentService service, SchedulesService schedulesService) {
        super(service);
        this.establishmentService = service;
        this.schedulesService = schedulesService;
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
    public ResponseEntity<BaseEstablishmentDTO> createEstablishment(@RequestBody BaseEstablishmentDTO establishmentDTO,
            Principal principal) {
        try {
            establishmentService.create(establishmentDTO, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(establishmentDTO);
        } catch (AlreadyExistsException e) {
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body(establishmentDTO);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishmentDTO);
        }
    }

    // GET /establishment/list?page=0&size=15&sort=id,desc
    @GetMapping("/list")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> listEstablishments(
            @ModelAttribute EstablishmentFilter filter,
            Pageable pageable) {
        BasePageDTO<EstablishmentDTO> listDTO = new BasePageDTO<>();
        try {
            listDTO.setItems(establishmentService.list(filter, pageable));
            for (EstablishmentDTO establishment : listDTO.getItems()) {
                Optional<ImageDTO> image = establishmentService
                        .getImages(establishment.getId(), PageRequest.of(0, 1, Sort.by("id").ascending())).stream()
                        .findFirst();
                if (image.isPresent()) {
                    ArrayList<ImageDTO> images = new ArrayList<>();
                    images.add(image.get());
                    establishment.setImages(images);
                }
            }
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
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

    @GetMapping("/{id}/services")
    public ResponseEntity<BasePageDTO<ServiceDTO>> listServices(
            @PathVariable Long id, Pageable pageable) {
        BasePageDTO<ServiceDTO> listDTO = new BasePageDTO<>();
        try {
            listDTO.setItems(establishmentService.listServices(id, pageable));
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
            establishmentService.addService(establishmentId, service);
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
    @PostMapping(path = "/{establishmentId}/images", consumes = "application/json")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addImages(@PathVariable("establishmentId") Long establishmentId,
            @RequestBody List<ImageDTO> images, Principal principal) {
        return super._addImages(establishmentId, images);
    }

    @GetMapping("/{establishmentId}/schedule")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BasePageDTO<ScheduleDTO>> getSchedules(
            @PathVariable("establishmentId") Long establishmentId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable, Principal principal) {
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
}
