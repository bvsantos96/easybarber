package com.teamsantos.easybarber.repositories.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.ServiceWithImagesDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceWithEmployeeFilter;
import com.teamsantos.easybarber.entities.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CustomServiceRepositoryImpl implements CustomServiceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ServiceWithImagesDTO> findAllWEmployee(ServiceWithEmployeeFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceWithImagesDTO> cq = cb.createQuery(ServiceWithImagesDTO.class);
        Root<Service> service = cq.from(Service.class);
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
        if (filter.isIncludeServiceImage()) {
            predicates.add(cb.equal(service.get("employee").get("images").get("main"), true));
        }
        if (filter.isIncludeEmployeeImage()) {
            predicates.add(cb.equal(service.get("service").get("images").get("main"), true));
        }
        cq.select(cb.construct(ServiceWithImagesDTO.class,
                service.get("id"),
                service.get("name"),
                service.get("description"),
                service.get("duration"),
                filter.isIncludeServiceImage() ? service.get("images").get("data") : null,
                service.get("serviceType").get("id"),
                service.get("serviceType").get("name"),
                service.get("serviceType").get("description"),
                filter.isIncludeServiceTypeImage() ? service.get("serviceType").get("imageURL") : null,
                service.get("employee").get("id"),
                service.get("employee").get("user").get("name"),
                filter.isIncludeEmployeeImage() ? service.get("employee").get("images").get("data") : null))
                .where(predicates.toArray(new Predicate[0]));

        TypedQuery<ServiceWithImagesDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ServiceWithImagesDTO> resultList = query.getResultList();
        long total = getTotalCount(predicates.toArray(new Predicate[0]), cb, service);
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<ServiceBaseDTO> findAllBase(ServiceFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceBaseDTO> cq = cb.createQuery(ServiceBaseDTO.class);
        Root<Service> service = cq.from(Service.class);
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
        if (filter.isIncludeServiceImage()) {
            predicates.add(cb.equal(service.get("employee").get("images").get("main"), true));
        }
        cq.select(cb.construct(ServiceBaseDTO.class,
                service.get("id"),
                service.get("name"),
                service.get("description"),
                service.get("duration"),
                filter.isIncludeServiceImage() ? service.get("images").get("data") : null,
                service.get("serviceType").get("id"),
                service.get("serviceType").get("name"),
                service.get("serviceType").get("description")))
                .where(predicates.toArray(new Predicate[0]));

        TypedQuery<ServiceBaseDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ServiceBaseDTO> resultList = query.getResultList();
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
