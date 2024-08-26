package com.teamsantos.easybarber.repositories.images;

import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.images.EmployeeImage;
import com.teamsantos.easybarber.repositories.base.ImageRepository;

@Repository
public interface EmployeeImageRepository extends ImageRepository<Employee, EmployeeImage> {
}
