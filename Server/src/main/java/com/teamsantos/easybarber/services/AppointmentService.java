package com.teamsantos.easybarber.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.AppointmentDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.repositories.AppoinmentRepository;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.UserRepository;

@Service
public class AppointmentService {
    private final AppoinmentRepository appoinmentRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AppointmentService(AppoinmentRepository appoinmentRepository, UserRepository userRepository,
            EmployeeRepository employeeRepository) {
        this.appoinmentRepository = appoinmentRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public void createAppointment(AppointmentDTO _appointmentDTO) throws Exception {
        if (_appointmentDTO.getUserID() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        Optional<User> userOpt = userRepository.findById(_appointmentDTO.getUserID());
        Optional<Employee> employeeOpt = employeeRepository.findById(_appointmentDTO.getEmployeeID());

        if (!userOpt.isPresent() || !employeeOpt.isPresent()) {
            throw new IllegalArgumentException("User ID must not be null");// TODO: change exception
        }

        User user = userOpt.get();
        Employee employee = employeeOpt.get();

    }
}
