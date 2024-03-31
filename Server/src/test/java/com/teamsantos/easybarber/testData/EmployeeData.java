package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class EmployeeData {
    public static final List<JSONObject> employees;

    static {
        employees = new ArrayList<>();
        try {
            employees.add(new JSONObject(
                    "{\"countryMobile\":\"+351\",\"mobile\":\"999999999\",\"password\":\"Test123*\",\"name\":\"Henrique\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
