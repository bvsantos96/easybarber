package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teamsantos.easybarber.DTO.image.ImageDTO;
import com.teamsantos.easybarber.testDTOs.UserTestDTO;

public class EmployeeData {
    public static final List<UserTestDTO> employees;
    public static final Map<Long, List<Long>> employeesEstablishments;
    public static final Map<Long, List<Long>> adminEstablishments;
    public static final Map<Long, List<ImageDTO>> employeeImages;

    static {
        employees = new ArrayList<>();
        employees.add(new UserTestDTO(2L, "+351", "999999999", "Test123*", "Henrique"));
        employees.add(new UserTestDTO(3L, "+351", "900000000", "Test123*", "Amigo do Joao"));
        employees.sort(Comparator.comparing(UserTestDTO::getId));

        employeesEstablishments = new TreeMap<>();
        employeesEstablishments.put(2L, new ArrayList<>());
        employeesEstablishments.get(2L).add(1L);
        employeesEstablishments.get(2L).add(2L);
        employeesEstablishments.put(3L, new ArrayList<>());
        employeesEstablishments.get(3L).add(2L);

        adminEstablishments = new TreeMap<>();
        adminEstablishments.put(2L, new ArrayList<>());
        adminEstablishments.get(2L).add(1L);
        adminEstablishments.put(3L, new ArrayList<>());
        adminEstablishments.get(3L).add(2L);

        employeeImages = new TreeMap<>();
        employeeImages.put(2L, new ArrayList<>() {
            {
                add(new ImageDTO(null,
                        "https://d2zdpiztbgorvt.cloudfront.net/region1/us/807905/biz_photo/c2da6e290fa84b0392079ca2ae658f-pedro-barber-biz-photo-cf22ec40162841139be5358ccd8193-booksy.jpeg",
                        true));
                add(new ImageDTO(null,
                        "https://www.ringmybarber.com/wp-content/uploads/2022/10/qualities-of-a-highly-professional-barber.jpg",
                        false));
            }
        });
        employeeImages.put(3L, new ArrayList<>() {
            {
                add(new ImageDTO(null,
                        "https://cdn.camberwellshopping.com.au/wp-content/uploads/2021/07/13111806/The-best-barbers-in-Camberwell.jpg",
                        true));
                add(new ImageDTO(null,
                        "https://www.josephguinbarber.com/uploads/1/2/4/4/124499791/josephguinhome_orig.jpg", false));
            }
        });
    }
}
