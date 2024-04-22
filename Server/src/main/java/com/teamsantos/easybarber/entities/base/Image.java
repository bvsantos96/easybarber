package com.teamsantos.easybarber.entities.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public class Image<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "entity_id", referencedColumnName = "id")
    private T entity;
    @Column
    private String data;
}
