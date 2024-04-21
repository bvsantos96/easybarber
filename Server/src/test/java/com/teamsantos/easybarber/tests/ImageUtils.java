package com.teamsantos.easybarber.tests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.utils.CreateTest;

public class ImageUtils {
    private final MockMvc mockMvc;
    private final String pathPrefix;

    @Autowired
    public ImageUtils(MockMvc mockMvc, String pathPrefix) {
        this.mockMvc = mockMvc;
        this.pathPrefix = pathPrefix;
    }

    private void addImage(String jwt, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, String.format("%s/images",pathPrefix), jwt, item);
    }

    public void saveImages(List<ImageDTO> images, String jwt) {
        try {
            for (ImageDTO image : images) {
                addImage(jwt, image.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
