package com.teamsantos.easybarber.DTO.filters;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private Long serviceId;
    private Set<DAY_OF_WEEK> dayOfWeek;
    private LocalDate from;
    private LocalDate to;
    private LocalDate _to;
    private LocalTime startHour;
    private LocalTime endHour;
    private Boolean active;
    private boolean parsed = false;

    public String generateURL(String _url) {
        boolean first = true;
        StringBuilder url = new StringBuilder(_url);
        if (employeeId != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("employeeId=").append(employeeId);
        }
        if (serviceId != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("serviceId=").append(serviceId);
        }
        if (establishmentId != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("establishmentId=").append(establishmentId);
        }
        if (dayOfWeek != null && !dayOfWeek.isEmpty()) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("dayOfWeek=");
            for (DAY_OF_WEEK day : dayOfWeek) {
                url.append(day.name()).append(",");
            }
            url.deleteCharAt(url.length() - 1);
        }
        if (from != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("from=").append(from);
        }
        if (to != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("to=").append(to);
        }
        if (startHour != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("startHour=").append(startHour);
        }
        if (endHour != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("endHour=").append(endHour);
        }
        if (active != null) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            url.append("active=").append(active);
        }
        return url.toString();
    }

    public void parseDate(Pageable pageable) throws Exception {
        if (parsed) {
            return;
        }
        parsed = true;
        if (getFrom() != null) {
            if (getTo() != null && getTo().isBefore(getFrom())) {
                LocalDate temp = getTo();
                setTo(getFrom());
                setFrom(temp);
            }
            setFrom(getFrom().plusDays(pageable.getPageNumber() * pageable.getPageSize()));
        }
        parseDate();
    }

    public void parseDate() throws Exception {
        if (parsed) {
            return;
        }
        parsed = true;
        if (getFrom() == null) {
            if (dayOfWeek == null || dayOfWeek.isEmpty()) {
                throw new Exception("No date or day of week specified");
            } else {
                setFrom(LocalDate.now());
                setTo(from.plusDays(getDayOfWeek().size()));
            }
        } else {
            if (getTo() == null) {
                if (getDayOfWeek() != null && !getDayOfWeek().isEmpty()) {
                    setTo(from.plusDays(getDayOfWeek().size()));
                } else {
                    setTo(getFrom());
                }
            }
            if (getDayOfWeek() != null && !getDayOfWeek().isEmpty()) {
                if (getTo() != null && getTo().isBefore(getFrom())) {
                    LocalDate temp = getTo();
                    setTo(getFrom());
                    setFrom(temp);
                }
                Set<DAY_OF_WEEK> days = new HashSet<>();
                int startDay = Utils.getDayOfWeek(getFrom()).ordinal();
                int nDays = (int) Math.min(7, getTo().toEpochDay() - getFrom().toEpochDay()) + 1;
                DAY_OF_WEEK[] DAY_OF_WEEK = EmployeeSchedule.DAY_OF_WEEK.values();
                days.add(DAY_OF_WEEK[startDay]);
                for (int i = 1; i < nDays; i++) {
                    days.add(DAY_OF_WEEK[(startDay + i) % 7]);
                }
                setDayOfWeek(days);
            } else {
                throw new Exception("No day of week specified");
            }
        }

    }

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

            if (this.getServiceId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("service").get("id"), this.getServiceId()));
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
        return getExceptionSpecification(true, true);
    }

    public Specification<ScheduleException> getExceptionSpecification(boolean filterEmployee,
            boolean filterEstablishment) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            boolean identifier = false;
            if (filterEmployee && this.getEmployeeId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("employee").get("id"), this.getEmployeeId()));
                identifier = true;
            }

            if (filterEstablishment && this.getEstablishmentId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("establishment").get("id"), this.getEstablishmentId()));
                identifier = true;
            }

            if (!identifier) {
                throw new IllegalArgumentException("Employee ID or Establishment ID is required");
            }

            if (this.getDayOfWeek() != null && !this.getDayOfWeek().isEmpty()) {
                predicates.add(root.get("day").in(this.getDayOfWeek()));
            }

            if (this.getServiceId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("service").get("id"), this.getServiceId()));
            }

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

    public long numberOfDays() throws Exception {
        parseDate();
        if (from == null || to == null) {
            return getDayOfWeek().size();
        }
        return ChronoUnit.DAYS.between(from, to) + 1;
    }
}
