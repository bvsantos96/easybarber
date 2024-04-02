package com.teamsantos.easybarber.testData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsersData {
    public static final List<JSONObject> users;

    static {
        users = new ArrayList<>();
        try {
            users.add(new JSONObject(
                    "{\"countryMobile\":\"+351\",\"mobile\":\"927030780\",\"password\":\"Test123*\",\"name\":\"Bruno Santos\"}"));
            users.add(new JSONObject(
                    "{\"countryMobile\":\"+351\",\"mobile\":\"962844407\",\"password\":\"Test123*\",\"name\":\"Filipe Santos\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
