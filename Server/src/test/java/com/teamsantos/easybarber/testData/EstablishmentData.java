package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.List;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;

public class EstablishmentData {
    public static final List<BaseEstablishmentDTO> establishments;
    public static final List<CreateEstablishmentServiceDTO> establishmentServices;
    public static final List<ImageDTO> establishmentImages;

    static {
        establishments = new ArrayList<>() {
            {
                add(new BaseEstablishmentDTO(1l, "Henrique Barber Shop", "Henrique Barber Shop", 38.62983, -9.19362));
                add(new BaseEstablishmentDTO(2L, "Forum ALmada Barber Shop", "Forum Almada Barber Shop", 38.65967,
                        -9.17385));
            }
        };
        establishmentServices = new ArrayList<>() {
            {
                ServiceDTO temp = ServiceData.services.get(0);
                add(new CreateEstablishmentServiceDTO(1L, temp.getId(),establishments.get(0).getId(), temp.getPrice(), true));
                temp = ServiceData.services.get(1);
                add(new CreateEstablishmentServiceDTO(2L, temp.getId(),establishments.get(0).getId(), temp.getPrice(), true));
                temp = ServiceData.services.get(2);
                add(new CreateEstablishmentServiceDTO(3L, temp.getId(),establishments.get(1).getId(), temp.getPrice(), true));
                add(new CreateEstablishmentServiceDTO(4L, temp.getId(),establishments.get(0).getId(), temp.getPrice(), true));
            }
        };
        establishmentImages = new ArrayList<>() {
            {
                add(new ImageDTO(1L, "image1"));
                add(new ImageDTO(2L, "image2"));
                add(new ImageDTO(3L, "image3"));
                add(new ImageDTO(4L, "image4"));
            }
        };
    }
}
