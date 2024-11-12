package com.teamsantos.easybarber.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class MessageLoader {

    private final ResourcePatternResolver resourcePatternResolver;
    private final Map<String, Map<String, String>> messagesMap = new HashMap<>();

    @Autowired
    public MessageLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @PostConstruct
    public void init() {
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:messages/*.properties");

            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (fileName != null) {
                    String baseFileName = fileName.replaceFirst("[.][^.]+$", "");

                    Properties properties = new Properties();
                    try (InputStream inputStream = resource.getInputStream();
                        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                        properties.load(reader);
                    }

                    Map<String, String> fileMap = properties.entrySet()
                            .stream()
                            .collect(Collectors.toMap(
                                    e -> e.getKey().toString(),
                                    e -> e.getValue().toString()
                            ));
                    messagesMap.put(baseFileName, fileMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Map<String, String>> getMessagesMap() {
        return messagesMap;
    }
}