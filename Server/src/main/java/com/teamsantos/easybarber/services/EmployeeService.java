package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.images.EmployeeImage;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.images.EmployeeImageRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class EmployeeService extends ServiceWithImages<Employee, EmployeeImage> {
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final UserService userService;
    private final ServiceRepository serviceRepository;

    @Autowired
    public EmployeeService(EmployeeRepository repository,
            EstablishmentStaffRepository establishmentStaffRepository, ServiceRepository serviceRepository,
            EmployeeImageRepository imageRepository,
            ModelMapper modelMapper, UserService userService) {
        super(repository, imageRepository, modelMapper);
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.serviceRepository = serviceRepository;
        this.userService = userService;
    }

    @Transactional
    public void deleteEmployee(Principal principal) {
        deleteEmployee(userService.getEmployee(principal));
    }

    @Transactional
    public void deleteEmployee(Employee employee) {
        establishmentStaffRepository.deleteByEmployeeId(employee.getId());
        serviceRepository.deleteByEmployeeId(employee.getId());
        employee.setEnabled(false);
        repository.save(employee);
        // TODO: Mark appointments as deleted and send notification to clients
    }
}
