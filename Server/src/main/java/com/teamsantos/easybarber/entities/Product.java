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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Product extends EntityWithImages<Product, ProductImage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "establishment", referencedColumnName = "id")
    private Establishment establishment;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_types", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "product_type_id"))
    @BatchSize(size = 10)
    private Set<ProductType> productTypes = new HashSet<>();
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
