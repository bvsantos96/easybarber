package com.teamsantos.easybarber.testData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EstablishmentData {
    public static final List<JSONObject> establishments;

    static {
        establishments = new ArrayList<>();
        try {
            establishments.add(new JSONObject(
                    "{ \"name\": \"Henrique Barber Shop\", \"description\": \"Henrique Barber Shop\", \"latitude\": 38.62983, \"longitude\": -9.19362 }"));
            establishments.add(new JSONObject(
                    "{ \"name\": \"Forum ALmada Barber Shop\", \"description\": \"Forum Almada Barber Shop\", \"latitude\": 38.65967, \"longitude\": -9.17385 }"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
