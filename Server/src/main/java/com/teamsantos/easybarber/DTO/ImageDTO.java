package com.teamsantos.easybarber.DTO;

import lombok.NoArgsConstructor;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO extends BaseDTO {
    private String data;

    public ImageDTO(Long id, String data) {
        super(id);
        this.data = data;
    }

    public ImageDTO(String data) {
        JSONObject json = new JSONObject(data);
        this.setId(json.getLong("id"));
        this.data = json.getString("data");
    }
}
