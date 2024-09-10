package com.teamsantos.easybarber.repositories.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.ServiceWithImagesDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceWithEmployeeFilter;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Service;
import com.teamsantos.easybarber.entities.images.ServiceImage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CustomServiceRepositoryImpl implements CustomServiceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ServiceWithImagesDTO> findAllWEmployee(ServiceWithEmployeeFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceWithImagesDTO> cq = cb.createQuery(ServiceWithImagesDTO.class);
        Root<Service> service = cq.from(Service.class);

        List<Predicate> predicates = constructBaseFilter(filter, cb, service);
        Join<Service, ServiceImage> imageJoin = null;
        if (filter.isIncludeServiceImage()) {
            imageJoin = service.join("images", JoinType.LEFT);
            imageJoin.on(cb.equal(imageJoin.get("entity").get("id"), service.get("id")));
            imageJoin.on(cb.equal(imageJoin.get("isMain"), true));
        }
        Join<Service, Employee> employeeJoin = service.join("employee",
                JoinType.INNER);
        // Join<Employee, EmployeeImage> employeeImageJoin = null;
        // if (filter.isIncludeEmployeeImage()) {
        // Join<Service, Employee> employeeJoin = service.join("employee",
        // JoinType.INNER);
        // employeeImageJoin = employeeJoin.join("employeeImages", JoinType.LEFT);
        // employeeImageJoin.on(cb.equal(employeeImageJoin.get("isMain"), true));
        // }

        cq.select(cb.construct(ServiceWithImagesDTO.class,
                service.get("id"),
                service.get("name"),
                service.get("description"),
                service.get("duration"),
                filter.isIncludeServiceImage() ? imageJoin.get("data") : null,
                service.get("serviceType").get("id"),
                service.get("serviceType").get("name"),
                service.get("serviceType").get("description"),
                service.get("serviceType").get("imageURL"),
                employeeJoin.get("id"),
                employeeJoin.get("user").get("name"),
                employeeJoin.get("user").get("email")))
                .where(predicates.toArray(new Predicate[0]));
        if (pageable.getSort().isSorted()) {
            List<Order> orders = pageable.getSort().stream()
                    .map(order -> order.isAscending()
                            ? cb.asc(service.get(order.getProperty()))
                            : cb.desc(service.get(order.getProperty())))
                    .collect(Collectors.toList());
            cq.orderBy(orders);
        }
        TypedQuery<ServiceWithImagesDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public List<ServiceBaseDTO> findAllBase(ServiceFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceBaseDTO> cq = cb.createQuery(ServiceBaseDTO.class);
        Root<Service> service = cq.from(Service.class);

        List<Predicate> predicates = constructBaseFilter(filter, cb, service);
        Join<Service, ServiceImage> imageJoin = null;
        if (filter.isIncludeServiceImage()) {
            imageJoin = service.join("images", JoinType.LEFT);
            imageJoin.on(cb.equal(imageJoin.get("isMain"), true));
        }

        cq.select(cb.construct(ServiceBaseDTO.class,
                service.get("id"),
                service.get("name"),
                service.get("description"),
                service.get("duration"),
                filter.isIncludeServiceImage() ? imageJoin.get("data") : null,
                service.get("serviceType").get("id"),
                service.get("serviceType").get("name"),
                service.get("serviceType").get("description"),
                service.get("serviceType").get("imageURL")))
                .where(predicates.toArray(new Predicate[0]));

        if (pageable.getSort().isSorted()) {
            List<Order> orders = pageable.getSort().stream()
                    .map(order -> order.isAscending() ? cb.asc(service.get(order.getProperty()))
                            : cb.desc(service.get(order.getProperty())))
                    .collect(Collectors.toList());
            cq.orderBy(orders);
        }
        TypedQuery<ServiceBaseDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    public long count(ServiceFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Service> serviceRoot = countQuery.from(Service.class);
        List<Predicate> predicates = constructBaseFilter(filter, cb, serviceRoot);
        countQuery.select(cb.count(serviceRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> constructBaseFilter(ServiceFilter filter, CriteriaBuilder cb, Root<Service> service) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getEmployeeId() != null) {
            predicates.add(cb.equal(service.get("employee").get("id"), filter.getEmployeeId()));
        }
        if (filter.getServiceTypeId() != null) {
            predicates.add(cb.equal(service.get("serviceType").get("id"), filter.getServiceTypeId()));
        }
        if (filter.getName() != null) {
            predicates.add(cb.like(cb.lower(service.get("name")), filter.getName().toLowerCase()));
        }
        if (filter.getDescription() != null) {
            predicates.add(
                    cb.like(cb.lower(service.get("description")), filter.getDescription().toLowerCase()));
        }
        return predicates;
    }

}
