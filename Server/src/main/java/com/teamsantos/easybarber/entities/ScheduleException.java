package com.teamsantos.easybarber.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.proxy.HibernateProxy;

import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalTime startHour;
    @Column
    private LocalTime endHour;
    @Column
    private Boolean active;
    @Column
    private DAY_OF_WEEK day;

    public ScheduleExceptionDTO toDTO() {
        return new ScheduleExceptionDTO(
                id,
                employee != null ? employee.getId() : null,
                establishment != null ? establishment.getId() : null,
                date,
                startHour,
                endHour,
                active,
                day);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
