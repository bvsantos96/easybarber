package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;

public class EstablishmentData {
    public static final List<BaseEstablishmentDTO> establishments;
    public static final List<CreateEstablishmentServiceDTO> establishmentServices;
    public static final Map<Long, List<ImageDTO>> establishmentImages;

    static {
        establishments = new ArrayList<>() {
            {
                add(new BaseEstablishmentDTO(1L, "Henrique Barber Shop", "Henrique Barber Shop", 38.62983, -9.19362));
                add(new BaseEstablishmentDTO(2L, "Forum Almada Barber Shop", "Forum Almada Barber Shop", 38.65967,
                        -9.17385));
            }
        };
        establishments.sort(Comparator.comparing(BaseEstablishmentDTO::getId));

        establishmentServices = new ArrayList<>() {
            {
                ServiceDTO temp = ServiceData.services.get(0);
                add(new CreateEstablishmentServiceDTO(1L, temp.getId(), establishments.get(0).getId(), temp.getPrice(),
                        true));
                temp = ServiceData.services.get(1);
                add(new CreateEstablishmentServiceDTO(2L, temp.getId(), establishments.get(0).getId(), temp.getPrice(),
                        true));
                temp = ServiceData.services.get(2);
                add(new CreateEstablishmentServiceDTO(3L, temp.getId(), establishments.get(1).getId(), temp.getPrice(),
                        true));
                temp = ServiceData.services.get(0);
                add(new CreateEstablishmentServiceDTO(4L, temp.getId(), establishments.get(1).getId(), temp.getPrice(),
                        true));
            }
        };
        establishmentServices.sort(Comparator.comparing(CreateEstablishmentServiceDTO::getId));

        establishmentImages = new TreeMap<>();
        establishmentImages.put(1L, new ArrayList<>() {
            {
                add(new ImageDTO(null,
                        "https://us-en-cdn.square.ncms.io/content/uploads/2022/10/BlackCat3.jpg.jpeg"));
                add(new ImageDTO(null,
                        "https://assets-global.website-files.com/644a9d9ce529ef8812f82a28/647fb85c69e95444243ef9bd_Henley%27s%20Gentlemen%27s%20Grooming%20-%20Barbershop%20and%20Mens%20Grooming.webp"));
            }
        });
        establishmentImages.put(2L, new ArrayList<>() {
            {
                add(new ImageDTO(null,
                        "https://img.freepik.com/premium-vector/barbershop-logo-barber-shop-logo-vector-template_664675-709.jpg"));
                add(new ImageDTO(null,
                        "https://images.squarespace-cdn.com/content/v1/6499eadde1c0a02a7d1be4ac/66036202-71d4-465f-b189-75fd80017d66/110A2577.jpg"));
            }
        });
    }
}
