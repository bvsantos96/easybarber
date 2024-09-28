package com.teamsantos.easybarber.DTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.Tuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeInformationDTO extends BaseResponseDTO {
    private String name;
    private String description;
    private String mobileNumber;
    private double rating;
    private long nvotes;
    private Set<Long> availableServices;
    private Set<ImageDTO> images;

    public EmployeeInformationDTO(long id, String name, String description, String mobileNumber, double rating,
            long nvotes, Set<Long> availableServices) {
        super(id);
        this.name = name;
        this.description = description;
        this.mobileNumber = mobileNumber;
        this.rating = rating;
        this.nvotes = nvotes;
        this.availableServices = availableServices;
    }

    public EmployeeInformationDTO(long id, String name, String description, String mobileNumber, double rating,
            long nvotes, Object availableServices, Object images) {
        super(id);
        this.name = name;
        this.description = description;
        this.mobileNumber = mobileNumber;
        this.rating = rating;
        this.nvotes = nvotes;
        if (availableServices != null) {
            this.availableServices = Arrays.stream(String.valueOf(availableServices).split(",")).map(Long::parseLong)
                    .collect(Collectors.toSet());
        }
        if (images != null) {
            this.images = new HashSet<>();
            String[] _images = String.valueOf(images).split(";");
            for (int i = 0; i < _images.length; i++) {
                String[] imageParts = _images[i].split(",");
                this.images.add(new ImageDTO(Long.parseLong(imageParts[0]), imageParts[2],
                        imageParts[1].equals("1")));
            }
        }
    }

    public EmployeeInformationDTO(Tuple tuple) {
        this(tuple.get(0, Long.class), tuple.get(1, String.class), tuple.get(2, String.class),
                tuple.get(3, String.class), tuple.get(4, BigDecimal.class).doubleValue(), tuple.get(5, Long.class),
                tuple.get(6), tuple.get(7));
    }
}
