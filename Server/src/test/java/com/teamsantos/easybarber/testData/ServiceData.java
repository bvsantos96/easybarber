package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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
                    "{\"name\":\"Haircut\",\"description\":\"Simple haircut\",\"imageUrl\":\"https://google.com\"}"));
            services.add(new JSONObject(
                    "{\"name\":\"Beard\",\"description\":\"Simple beard trim\",\"imageUrl\":\"https://google.com\"}"));
            services.add(new JSONObject(
                    "{\"name\":\"Beard and haircut\",\"description\":\"Simple haircut and beard trim\",\"imageUrl\":\"https://google.com\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
