package com.teamsantos.easybarber.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.repositories.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, UserService userService) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
    }

    public void deleteEmployee(Principal principal) {
        deleteEmployee(userService.getEmployee(principal));
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        deleteEmployee(employee);
    }

    @Transactional
    public void deleteEmployee(Employee employee) {
        employee.setEnabled(false);
        employeeRepository.save(employee);
        // TODO: Mark appointments as deleted and send notification to clients
    }
}
