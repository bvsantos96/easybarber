package com.teamsantos.easybarber.DTO.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFilter {
    private Long employeeId;
    private Long establishmentId;
    private Set<DAY_OF_WEEK> dayOfWeek;
    private Date from;
    private Date to;
    private String startHour;
    private String endHour;
    private Boolean active;

    public Specification<EmployeeSchedule> getSpecification() {
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

            if (!identifier) {
                throw new IllegalArgumentException("Employee ID or Establishment ID is required");
            }

            if (this.getDayOfWeek() != null && !this.getDayOfWeek().isEmpty()) {
                predicates.add(root.get("day").in(this.getDayOfWeek()));
            } else {
                predicates.add(root.get("day").in(Utils.getTodayDayOfWeek()));
            }

            if (this.getStartHour() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startHour"), this.getStartHour()));
            }

            if (this.getEndHour() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endHour"), this.getEndHour()));
            }

            if (this.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), this.getActive()));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("active"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
