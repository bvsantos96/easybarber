package com.teamsantos.easybarber.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ProductRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "products_requested", joinColumns = @JoinColumn(name = "product_request_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @BatchSize(size = 10)
    private Set<Product> products = new HashSet<>();
}
