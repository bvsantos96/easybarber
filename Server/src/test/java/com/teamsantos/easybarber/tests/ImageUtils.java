package com.teamsantos.easybarber.tests;

import java.util.List;

import org.json.JSONObject;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.Utils;

public class ImageUtils {
    private final MockMvc mockMvc;
    private final String pathPrefix;

    public ImageUtils(MockMvc mockMvc, String pathPrefix) {
        this.mockMvc = mockMvc;
        this.pathPrefix = pathPrefix;
    }

    private void addImages(String jwt, List<ImageDTO> images) {
        try {
            addImages(jwt, Utils.fromListToString(images));
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    private void addImages(String jwt, String items) throws Exception {
        CreateTest.createOrFound(mockMvc, String.format("%s/images", pathPrefix), jwt, items);
    }

    public void saveImages(List<ImageDTO> images, String jwt) {
        try {
            addImages(jwt, images);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    public List<ImageDTO> getImages(String jwt) {
        try {
            ResultActions result = CreateTest.get(mockMvc, String.format("%s/images?sort=id", pathPrefix), jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            return JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ImageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
        return null;
    }
}
