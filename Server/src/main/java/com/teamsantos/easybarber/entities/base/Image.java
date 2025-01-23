package com.teamsantos.easybarber.entities.base;

import com.teamsantos.easybarber.DTO.image.ImageDTO;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class Image<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "entity_id", referencedColumnName = "id")
    private T entity;
    @Column
    private String data; //data here is actually the url but will stay as data because all the image architecture was mounted expecting data and would require too much effort to change and data in this context is fine i think
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isMain;

    public ImageDTO convertToDTO() {
        return new ImageDTO(id, data, isMain);
    }
}
