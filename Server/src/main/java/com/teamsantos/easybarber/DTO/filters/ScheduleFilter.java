package com.teamsantos.easybarber.DTO.filters;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.ScheduleException;
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
    private LocalDate from;
    private LocalDate to;
    private LocalDate _to;
    private String startHour;
    private String endHour;
    private Boolean active;
    private boolean parsed = false;

    private void _parseDate() {
        if (parsed) {
            return;
        }

        if (from.isAfter(to)) {
            LocalDate temp = to;
            to = from;
            from = temp;
        }

        if (getDayOfWeek() == null || getDayOfWeek().isEmpty()) {
            Set<DAY_OF_WEEK> days = new HashSet<>();
            int startDay = Utils.getDayOfWeek(getFrom()).ordinal();
            int nDays = (int) Math.min(7, getTo().toEpochDay() - getFrom().toEpochDay());
            DAY_OF_WEEK[] DAY_OF_WEEK = EmployeeSchedule.DAY_OF_WEEK.values();
            for (int i = 1; i < nDays; i++) {
                days.add(DAY_OF_WEEK[(startDay + i) % 7]);
            }
            setDayOfWeek(days);
        }

        if (getStartHour() == null) {
            setStartHour(Utils.getTimeNow("HH:mm"));
        }

        if (getEndHour() == null) {
            setEndHour(Utils.getTimeNow("HH:mm"));
        }

        parsed = true;
    }

    public void parseDate(Pageable pageable) {
        setFrom(this.getFrom().plusDays(pageable.getOffset()));
        if (this.getTo() == null) {
            setTo(this.getFrom());
            set_to(this.getFrom());
        } else {
            LocalDate _to = from.plusDays(pageable.getPageSize());
            set_to(this.getTo());
            if (this.getTo().isAfter(_to)) {
                setTo(_to);
            }

        }
        _parseDate();
    }

    public void parseDate() {
        if (getFrom() == null) {
            setFrom(LocalDate.now());
        }

        if (getTo() == null) {
            setTo(LocalDate.now());
        }
        _parseDate();
    }

    public Specification<EmployeeSchedule> getSpecification() {
        parseDate();
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

            predicates.add(root.get("day").in(this.getDayOfWeek()));

            if (this.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), this.getActive()));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("active"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<ScheduleException> getExceptionSpecification() {
        parseDate();
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

            predicates.add(root.get("day").in(this.getDayOfWeek()));

            if (this.getFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), this.getFrom()));
            }

            if (this.getTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), this.getTo()));
            }

            if (this.getStartHour() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startHour"), this.getStartHour()));
            }

            if (from == to) {
                if (this.getEndHour() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endHour"), this.getEndHour()));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public long numberOfDays() {
        parseDate();
        return ChronoUnit.DAYS.between(from, to) + 1;
    }
}
