package com.teamsantos.easybarber.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.employee.EmployeeDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.images.EmployeeImage;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.images.EmployeeImageRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;

@Service
public class EmployeeService extends ServiceWithImages<Employee, EmployeeImage> {
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository repository,
            EstablishmentStaffRepository establishmentStaffRepository,
            ServiceRepository serviceRepository,
            EmployeeImageRepository imageRepository,
            EmployeeRepository employeeRepository,
            ModelMapper modelMapper, EntityManager entityManager) {
        super(repository, imageRepository, modelMapper, entityManager);
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void deleteEmployee() {
        Long id = UserContext.getCurrentUser().getEmployeeId();
        if (id == null) {
            throw new UserNotFoundException();
        }
        establishmentStaffRepository.deleteByEmployeeId(id);
        serviceRepository.deleteByEmployeeId(id);
        ((EmployeeRepository) repository).markAsDeleted(id);
        // TODO: Mark appointments as deleted and send notification to clients
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployee(long id) {
        return Utils.getModelMapper().map(employeeRepository.findById(id)
                .orElseThrow(UserNotFoundException::new), EmployeeDTO.class);
    }

    @Transactional(readOnly = true)
    public long getUserId(long employeeId) {
        Long userId = employeeRepository.findUserIdById(employeeId);
        if (userId == null) {
            throw new UserNotFoundException();
        }
        return userId;
    }

    @Transactional(readOnly = true)
    public Long getEmployeeId(Long establishmentStaffId) {
        return establishmentStaffRepository.getEmployeeId(establishmentStaffId);
    }

    @Transactional
    public void addFeedback(Long id, int feedback, boolean replace) {
        employeeRepository.findById(id).ifPresent(employee -> {
            employee.setSumVotes(employee.getSumVotes() + feedback);
            if (!replace) {
                employee.setNVotes(employee.getNVotes() + 1);
            }
            employeeRepository.save(employee);
        });
    }
}
