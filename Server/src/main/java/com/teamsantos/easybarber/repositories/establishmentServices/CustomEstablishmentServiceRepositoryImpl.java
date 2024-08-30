package com.teamsantos.easybarber.repositories.establishmentServices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.BaseDTO;
import com.teamsantos.easybarber.DTO.EstablishmentServiceBaseDTO;
import com.teamsantos.easybarber.DTO.EstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.filters.EstablishmentServiceFilter;
import com.teamsantos.easybarber.entities.EstablishmentService;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CustomEstablishmentServiceRepositoryImpl implements CustomEstablishmentServiceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<EstablishmentService> establishmentService,
            EstablishmentServiceFilter filter) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getEstablishmentId() != null) {
            predicates.add(cb.equal(establishmentService.get("establishment").get("id"), filter.getEstablishmentId()));
        }

        if (filter.getServiceTypeId() != null) {
            predicates.add(cb.equal(establishmentService.get("service").get("serviceType").get("id"),
                    filter.getServiceTypeId()));
        }

        if (filter.getEmployeeId() != null) {
            predicates.add(
                    cb.equal(establishmentService.get("service").get("employee").get("id"), filter.getEmployeeId()));
        }

        if (filter.getName() != null) {
            predicates.add(cb.like(cb.lower(establishmentService.get("service").get("name")),
                    Utils.formatStringToLIKE(filter.getName())));
        }

        if (filter.getDescription() != null) {
            predicates.add(cb.like(cb.lower(establishmentService.get("service").get("description")),
                    Utils.formatStringToLIKE(filter.getDescription())));
        }

        if (filter.isIncludeServiceImage()) {
            predicates.add(cb.equal(establishmentService.get("service").get("images").get("main"), true));
        }

        if (filter.isIncludeEmployeeImage()) {
            predicates
                    .add(cb.equal(establishmentService.get("service").get("employee").get("images").get("main"), true));
        }

        if (filter.isIncludeEstablishmentImage()) {
            predicates.add(cb.equal(establishmentService.get("establishment").get("images").get("main"), true));
        }

        return predicates;
    }

    private <T extends BaseDTO> Page<T> buildPage(
            CriteriaBuilder cb,
            CriteriaQuery<T> cq,
            Root<EstablishmentService> establishmentService,
            List<Predicate> predicates,
            Pageable pageable) {
        TypedQuery<T> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<T> resultList = query.getResultList();
        long total = getTotalCount(predicates.toArray(new Predicate[0]), cb, establishmentService);
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<EstablishmentServiceDTO> findAll(EstablishmentServiceFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EstablishmentServiceDTO> cq = cb.createQuery(EstablishmentServiceDTO.class);

        Root<EstablishmentService> establishmentService = cq.from(EstablishmentService.class);
        List<Predicate> predicates = buildPredicates(cb, establishmentService, filter);
        cq.select(cb.construct(EstablishmentServiceDTO.class,
                establishmentService.get("id"),
                establishmentService.get("service").get("id"),
                establishmentService.get("service").get("name"),
                establishmentService.get("service").get("description"),
                establishmentService.get("service").get("duration"),
                filter.isIncludeServiceImage() ? establishmentService.get("service").get("images").get("data") : null,
                establishmentService.get("service").get("serviceType").get("id"),
                establishmentService.get("service").get("serviceType").get("name"),
                establishmentService.get("service").get("serviceType").get("description"),
                establishmentService.get("service").get("employee").get("id"),
                establishmentService.get("service").get("employee").get("name"),
                filter.isIncludeEmployeeImage()
                        ? establishmentService.get("service").get("employee").get("images").get("data")
                        : null,
                establishmentService.get("establishment").get("id"),
                establishmentService.get("establishment").get("name"),
                establishmentService.get("establishment").get("description"),
                filter.isIncludeEstablishmentImage()
                        ? establishmentService.get("establishment").get("images").get("data")
                        : null,
                establishmentService.get("establishment").get("price"),
                establishmentService.get("establishment").get("active")));

        return buildPage(cb, cq, establishmentService, predicates, pageable);
    }

    @Override
    public Page<EstablishmentServiceBaseDTO> findAllBase(EstablishmentServiceFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EstablishmentServiceBaseDTO> cq = cb.createQuery(EstablishmentServiceBaseDTO.class);

        Root<EstablishmentService> establishmentService = cq.from(EstablishmentService.class);
        List<Predicate> predicates = buildPredicates(cb, establishmentService, filter);

        cq.select(cb.construct(EstablishmentServiceBaseDTO.class,
                establishmentService.get("id"),
                establishmentService.get("service").get("id"),
                establishmentService.get("service").get("name"),
                establishmentService.get("service").get("description"),
                establishmentService.get("service").get("duration"),
                filter.isIncludeServiceImage() ? establishmentService.get("service").get("images").get("data") : null,
                establishmentService.get("service").get("serviceType").get("id"),
                establishmentService.get("service").get("serviceType").get("name"),
                establishmentService.get("service").get("serviceType").get("description"),
                establishmentService.get("service").get("employee").get("id"),
                establishmentService.get("service").get("employee").get("name"),
                filter.isIncludeEmployeeImage()
                        ? establishmentService.get("service").get("employee").get("images").get("data")
                        : null,
                establishmentService.get("establishment").get("id"),
                establishmentService.get("establishment").get("price"),
                establishmentService.get("establishment").get("active")));

        return buildPage(cb, cq, establishmentService, predicates, pageable);
    }

    private long getTotalCount(Predicate[] predicates, CriteriaBuilder cb, Root<EstablishmentService> service) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(service));
        countQuery.where(predicates);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public Page<ServiceDTO> findAllServiceDTO(EstablishmentServiceFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceDTO> cq = cb.createQuery(ServiceDTO.class);

        Root<EstablishmentService> establishmentService = cq.from(EstablishmentService.class);
        List<Predicate> predicates = buildPredicates(cb, establishmentService, filter);

        cq.select(cb.construct(ServiceDTO.class,
                establishmentService.get("id"),
                establishmentService.get("service").get("employee").get("id"),
                establishmentService.get("service").get("serviceType").get("id"),
                establishmentService.get("service").get("name"),
                establishmentService.get("price"),
                establishmentService.get("service").get("description"),
                establishmentService.get("service").get("duration")));

        return buildPage(cb, cq, establishmentService, predicates, pageable);
    }
}
