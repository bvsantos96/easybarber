package com.teamsantos.easybarber.entities;

import java.time.LocalTime;

import org.hibernate.proxy.HibernateProxy;

import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;

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
public class EmployeeSchedule {
    public static enum DAY_OF_WEEK {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false, name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(nullable = true, name = "establishment_id", referencedColumnName = "id")
    private Establishment establishment;
    @Column
    private DAY_OF_WEEK day;
    @Column
    private LocalTime startHour;
    @Column
    private LocalTime endHour;
    @Column
    private boolean active;

    public ScheduleDTO toDTO() {
        return new ScheduleDTO(id, employee.getId(), establishment.getId(), day, startHour, endHour);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
