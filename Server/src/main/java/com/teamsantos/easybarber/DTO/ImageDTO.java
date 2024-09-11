package com.teamsantos.easybarber.DTO;

import org.json.JSONObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO extends BaseDTO {
    private String data;
    private Boolean main;

    public ImageDTO(Long id, String data, Boolean isMain) {
        super(id);
        this.data = data;
        this.main = isMain;
    }

    public ImageDTO(Long id, String data) {
        this(id, data, null);
    }

    public ImageDTO(String data) {
        JSONObject json = new JSONObject(data);
        if (json.has("id")) {
            Long _id = json.getLong("id");
            this.setId(_id.equals(0L) ? null : _id);
        }
        if (json.has("data"))
            this.data = json.getString("data");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ImageDTO other = (ImageDTO) obj;
        return this.data.equals(other.getData())
                && this.main.equals(other.getMain());
    }
}
