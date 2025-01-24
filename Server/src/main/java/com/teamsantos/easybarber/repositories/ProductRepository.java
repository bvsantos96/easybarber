package com.teamsantos.easybarber.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.filters.ProductFilter;
import com.teamsantos.easybarber.DTO.product.ProductDTO;
import com.teamsantos.easybarber.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.product.ProductDTO(
                    p.id,
                    p.establishment.id,
                    p.employee.id,
                    (SELECT new java.util.HashSet<Long>(pt.id)
                     FROM ProductType pt
                     WHERE pt MEMBER OF p.productTypes),
                    p.name,
                    p.description,
                    p.price,
                    i.data
                )
                FROM Product p
                LEFT JOIN ProductImages i ON i.isMain = true and i.entity.id = p.id
                WHERE (:#{#filter.employeeId} is null or p.employee.id = :#{#filter.employeeId})
                AND (:#{#filter.establishmentId} is null or p.establishment.id = :#{#filter.establishmentId})
                AND (:#{#filter.name} is null or lower(p.name) like lower(:#{#filter.name}))
                AND (:#{#filter.description} is null or lower(p.description) like lower(:#{#filter.description}))
            """)
    Page<ProductDTO> getProducts(@Param("filter") ProductFilter filter, Pageable pageable);
}
