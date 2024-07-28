package com.teamsantos.easybarber.entities;

import java.time.LocalDate;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ScheduleException {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = true, name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(nullable = true, name = "establishment_id", referencedColumnName = "id")
    private Establishment establishment;
    @Column
    private LocalDate date;
    @Column
    private String startHour;
    @Column
    private String endHour;
    @Column
    private Boolean active;
    @Column
    private DAY_OF_WEEK day;
}
