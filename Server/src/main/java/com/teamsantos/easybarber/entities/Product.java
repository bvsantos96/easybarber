package com.teamsantos.easybarber.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.images.ProductImage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = { @Index(columnList = "establishment_id"), @Index(columnList = "employee_id"),
        @Index(columnList = "product_type_ids"), @Index(columnList = "price"), @Index(columnList = "name") })
public class Product extends EntityWithImages<Product, ProductImage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Establishment establishment;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_type_ids", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_type_id"))
    @BatchSize(size = 10)
    private Set<ProductType> productTypes = new HashSet<>();
    @Column(nullable = true)
    private Employee employee;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private boolean available = true;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<ProductSuggestions> suggestions;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<ProductRequest> requests;

    @Override
    public Product getEntity() {
        return this;
    }

    public void addProductType(ProductType productType) {
        if (productTypes == null) {
            productTypes = new HashSet<>();
        }
        productTypes.add(productType);
    }
}
