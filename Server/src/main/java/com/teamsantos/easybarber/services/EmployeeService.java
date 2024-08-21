package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.images.EmployeeImage;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.images.EmployeeImageRepository;
import com.teamsantos.easybarber.utils.Utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class EmployeeService extends ServiceWithImages<Employee, EmployeeImage> {
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final UserService userService;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository repository,
            EstablishmentStaffRepository establishmentStaffRepository, ServiceRepository serviceRepository,
            EmployeeImageRepository imageRepository,
            ModelMapper modelMapper, UserService userService, EmployeeRepository employeeRepository) {
        super(repository, imageRepository, modelMapper);
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.serviceRepository = serviceRepository;
        this.userService = userService;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void deleteEmployee(Principal principal) {
        deleteEmployee(userService.getEmployeeEntity(principal));
    }

    @Transactional
    public void deleteEmployee(Employee employee) {
        establishmentStaffRepository.deleteByEmployeeId(employee.getId());
        serviceRepository.deleteByEmployeeId(employee.getId());
        employee.setEnabled(false);
        repository.save(employee);
        // TODO: Mark appointments as deleted and send notification to clients
    }

    @Cacheable(value = "employeeByMobileInformation", key = "#mobileInformation")
    public EmployeeDTO getEmployee(String mobileInformation) {
        return Utils.getModelMapper().map(employeeRepository.findByMobileInformation(mobileInformation)
                .orElseThrow(UserNotFoundException::new), EmployeeDTO.class);
    }

    @Cacheable(value = "employeeById", key = "#id")
    public EmployeeDTO getEmployee(long id) {
        return Utils.getModelMapper().map(employeeRepository.findById(id)
                .orElseThrow(UserNotFoundException::new), EmployeeDTO.class);
    }

    @Cacheable(value = "employeeIdByMobileInformation", key = "#mobileInformation")
    public Long getEmployeeIdByMobileInformation(String mobileInformation) {
        return getEmployee(mobileInformation).getId();
    }
}
