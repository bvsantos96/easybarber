package com.teamsantos.easybarber.entities.images;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.base.Image;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = { @Index(columnList = "entity_id, is_main"), @Index(columnList = "entity_id, data") })
public class EmployeeImage extends Image<Employee, EmployeeImage> {
}
