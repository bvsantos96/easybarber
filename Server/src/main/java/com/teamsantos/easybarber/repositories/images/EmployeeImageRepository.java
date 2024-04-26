package com.teamsantos.easybarber.repositories.images;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.images.EmployeeImage;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeImageRepository extends ImageRepository<Employee, EmployeeImage> {
}
