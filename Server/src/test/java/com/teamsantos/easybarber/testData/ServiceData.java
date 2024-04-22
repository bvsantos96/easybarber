package com.teamsantos.easybarber.testData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceData {
    public static final List<JSONObject> serviceTypes;
    public static final List<JSONObject> services;

    static {
        serviceTypes = new ArrayList<>();
        services = new ArrayList<>();
        try {
            serviceTypes.add(new JSONObject(
                    "{\"name\":\"Haircut\",\"description\":\"Simple haircut\",\"imageUrl\":\"https://google.com\"}"));
            serviceTypes.add(new JSONObject(
                    "{\"name\":\"Beard\",\"description\":\"Simple beard trim\",\"imageUrl\":\"https://google.com\"}"));
            serviceTypes.add(new JSONObject(
                    "{\"name\":\"Beard and haircut\",\"description\":\"Simple haircut and beard trim\",\"imageUrl\":\"https://google.com\"}"));

            services.add(new JSONObject(
                    "{\"name\":\"Haircut\",\"description\":\"Simple haircut\",\"imageUrl\":\"https://youtube.com\", \"serviceTypeId\":1, \"price\": 10.0, \"employeeId\":1}"));
            services.add(new JSONObject(
                    "{\"name\":\"Beard\",\"description\":\"Simple beard trim\",\"imageUrl\":\"https://youtube.com\" , \"serviceTypeId\":2, \"price\": 5.0, \"employeeId\":1}"));
            services.add(new JSONObject(
                    "{\"name\":\"Beard and haircut\",\"description\":\"Simple haircut and beard trim\",\"imageUrl\":\"https://youtube.com\", \"serviceTypeId\":3, \"price\": 15.0, \"employeeId\":1}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
