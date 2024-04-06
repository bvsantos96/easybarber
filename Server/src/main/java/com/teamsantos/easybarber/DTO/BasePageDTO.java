package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
@Data
@EqualsAndHashCode(callSuper = true)
public class BasePageDTO<T> extends BaseResponseDTO {
    Page<T> items;

    public BasePageDTO() {
    }

    public BasePageDTO(Page<T> items) {
        this.items = items;
    }

    public BasePageDTO(String message) {
        super(message);
    }

    public void loadFromJSON(String json) throws Exception {
        JSONObject obj = new JSONObject(json);
        obj = obj.getJSONObject("items");
        JSONArray arr = obj.getJSONArray("content");
        for(int i = 0; i < arr.length(); i++) {
            BaseDTO item = new BaseDTO();
            item.loadFromJSON(arr.getJSONObject(i).toString());
        }
    }
}
