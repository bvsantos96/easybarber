package com.teamsantos.easybarber.DTO.establishment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.geolatte.geom.Point;

import com.teamsantos.easybarber.DTO.image.ImageDTO;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstablishmentInformationDTO extends EstablishmentDTO {
    private Set<Long> availableServices;
    private double rating;

    public EstablishmentInformationDTO(long id, String name, String description, String address,
            Point<?> location,
            Object images, long nVotes, double rating, Object availableServices) {
        super(id, name, description, address, nVotes, (long) (rating * nVotes));
        if (location != null && location.getPosition() != null) {
            this.setLatitude(location.getPosition().getCoordinate(1));
            this.setLongitude(location.getPosition().getCoordinate(0));
        }
        this.setRating(rating);
        if (availableServices != null) {
            this.availableServices = Arrays.stream(String.valueOf(availableServices).split(",")).map(Long::parseLong)
                    .collect(Collectors.toSet());
        }
        if (images != null) {
            Set<ImageDTO> imagesSet = new HashSet<>();
            String[] _images = String.valueOf(images).split(";");
            for (int i = 0; i < _images.length; i++) {
                String[] imageParts = _images[i].split(",");
                imagesSet.add(new ImageDTO(Long.parseLong(imageParts[0]), imageParts[2],
                        imageParts[1].equals("1")));
            }
            this.setImages(imagesSet);
        }
    }

    public EstablishmentInformationDTO(Tuple tuple) {
        this(tuple.get(0, Long.class), tuple.get(1, String.class), tuple.get(2, String.class),
                tuple.get(3, String.class), tuple.get(4, Point.class), tuple.get(5, Object.class),
                tuple.get(6, Long.class), tuple.get(7, BigDecimal.class).doubleValue(), tuple.get(8, Object.class));
    }
}
