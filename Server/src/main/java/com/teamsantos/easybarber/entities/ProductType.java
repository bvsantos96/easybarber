package com.teamsantos.easybarber.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String imageURL;
    @ManyToMany(mappedBy = "productTypes")
    private Set<Product> products = new HashSet<>();
    // @ManyToMany(mappedBy = "parentTypes")
    // private Set<ProductType> childTypes = new HashSet<>();
    // @ManyToMany(mappedBy = "childTypes")
    // private Set<ProductType> parentTypes = new HashSet<>();
}
