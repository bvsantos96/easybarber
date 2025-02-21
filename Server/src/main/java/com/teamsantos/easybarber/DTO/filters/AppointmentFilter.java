package com.teamsantos.easybarber.DTO.filters;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.teamsantos.easybarber.entities.Appointment;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFilter {
    private Long employeeId;
    private Long establishmentId;
    private Long clientId;
    private Long serviceId;
    private LocalDate date;
    private LocalDate endDate;
    private LocalTime time;
    private LocalTime endTime;
    Boolean userView;
    Boolean future;
    Boolean activeOnly;

    public Specification<Appointment> getSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            boolean identifier = false;
            if (this.getEmployeeId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("employee").get("id"), this.getEmployeeId()));
                identifier = true;
            }

            if (this.getEstablishmentId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("establishment").get("id"), this.getEstablishmentId()));
                identifier = true;
            }

            if (this.getClientId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), this.getClientId()));
                identifier = true;
            }

            if (!identifier) {
                throw new IllegalArgumentException("Employee, Establishment or User are required");
            }

            if (this.getServiceId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("service").get("id"), this.getServiceId()));
            }

            if (this.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), this.getEndDate()));
                if (this.getDate() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), this.getDate()));
                }
            } else {
                if (this.getDate() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("date"), this.getDate()));
                }
            }

            if (this.getTime() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time"), this.getTime()));
            }

            if (this.getEndTime() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), this.getEndTime()));
            }

            if (this.activeOnly != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), this.activeOnly));
            }

            if (this.future != null) {
                if (this.future) {
                    predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("active"), true),
                            criteriaBuilder.or(
                                    criteriaBuilder.greaterThan(root.get("date"), LocalDate.now()),
                                    criteriaBuilder.and(
                                            criteriaBuilder.equal(root.get("date"), LocalDate.now()),
                                            criteriaBuilder.greaterThanOrEqualTo(root.get("time"), LocalTime.now())))));
                } else {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("active"), false),
                            criteriaBuilder.or(
                                    criteriaBuilder.lessThan(root.get("date"), LocalDate.now()),
                                    criteriaBuilder.and(
                                            criteriaBuilder.equal(root.get("date"), LocalDate.now()),
                                            criteriaBuilder.lessThan(root.get("time"), LocalTime.now())))));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
