package com.teamsantos.easybarber.repositories.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Service;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.entities.images.ServiceImage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class CustomServiceRepositoryImpl implements CustomServiceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ServiceWithEmployeeDTO> findAll(ServiceFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceWithEmployeeDTO> cq = cb.createQuery(ServiceWithEmployeeDTO.class);
        Root<Service> service = cq.from(Service.class);

        Join<Service, Employee> employeeJoin = service.join("employee", JoinType.LEFT);
        Join<Service, ServiceType> serviceTypeJoin = service.join("serviceType", JoinType.LEFT);

        Subquery<byte[]> imageSubquery = cq.subquery(byte[].class);
        Root<ServiceImage> imageRoot = imageSubquery.from(ServiceImage.class);
        imageSubquery.select(imageRoot.get("data"));
        imageSubquery.where(
                cb.equal(imageRoot.get("entity").get("id"), service.get("id")),
                cb.isTrue(imageRoot.get("isMain")));

        List<Predicate> predicates = new ArrayList<>();
        if (filter.getEmployeeId() != null) {
            predicates.add(cb.equal(employeeJoin.get("id"), filter.getEmployeeId()));
        }
        if (filter.getServiceTypeId() != null) {
            predicates.add(cb.equal(serviceTypeJoin.get("id"), filter.getServiceTypeId()));
        }
        if (filter.getName() != null) {
            predicates.add(cb.like(cb.lower(service.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        cq.select(cb.construct(ServiceWithEmployeeDTO.class,
                service.get("id"),
                service.get("name"),
                service.get("description"),
                service.get("duration"),
                serviceTypeJoin.get("id"),
                imageSubquery,
                employeeJoin.get("id"),
                employeeJoin.get("user").get("name"))).where(predicates.toArray(new Predicate[0]));

        // Execute query
        TypedQuery<ServiceWithEmployeeDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ServiceWithEmployeeDTO> resultList = query.getResultList();
        long total = getTotalCount(predicates.toArray(new Predicate[0]), cb, service);

        return new PageImpl<>(resultList, pageable, total);
    }

    private long getTotalCount(Predicate[] predicates, CriteriaBuilder cb, Root<Service> service) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(service));
        countQuery.where(predicates);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
