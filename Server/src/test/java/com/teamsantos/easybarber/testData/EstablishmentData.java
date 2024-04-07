package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.List;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;

public class EstablishmentData {
    public static final List<BaseEstablishmentDTO> establishments;

    static {
        establishments = new ArrayList<>() {
            {
                add(new BaseEstablishmentDTO(1l, "Henrique Barber Shop", "Henrique Barber Shop", 38.62983, -9.19362));
                add(new BaseEstablishmentDTO(2L, "Forum ALmada Barber Shop", "Forum Almada Barber Shop", 38.65967,
                        -9.17385));
            }
        };
    }
}
