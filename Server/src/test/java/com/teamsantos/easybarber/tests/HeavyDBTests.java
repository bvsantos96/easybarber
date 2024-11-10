package com.teamsantos.easybarber.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.teamsantos.easybarber.utils.CreateTest;

@SpringBootTest
@AutoConfigureMockMvc
public class HeavyDBTests {
    @Value("${teamsantos.istestheavy}")
    private boolean isTestContext;

    private final MockMvc mockMvc;

    @Autowired
    public HeavyDBTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create() throws Exception {
        if (!isTestContext) {
            return;
        }
        CreateTest.get(mockMvc, "/createheavydb");
    }
}
