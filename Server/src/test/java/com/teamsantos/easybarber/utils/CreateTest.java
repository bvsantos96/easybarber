package com.teamsantos.easybarber.utils;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class CreateTest {
    public static ResultActions delete(MockMvc mockMvc, String path, String jwt) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .delete(path)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public static ResultActions delete(MockMvc mockMvc, String path, String jwt, String data) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .delete(path)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data));
    }

    public static void deleteOk(MockMvc mockMvc, String path, String jwt, String data) throws Exception {
        ResultActions result = delete(mockMvc, path, jwt, data);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
    }

    public static ResultActions put(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        try {
            return mockMvc.perform(MockMvcRequestBuilders
                    .put(path)
                    .header("Authorization", "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(item));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultActions putWJWT(MockMvc mockMvc, String path, String jwt) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .put(path)
                .header("Authorization", "Bearer " + jwt));
    }

    public static ResultActions put(MockMvc mockMvc, String path, String item) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(item));
    }

    public static ResultActions putBadRequestWJWT(MockMvc mockMvc, String path, String jwt) throws Exception {
        ResultActions result = putWJWT(mockMvc, path, jwt);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest());
        return result;
    }

    public static ResultActions putBadRequest(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        ResultActions result = put(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest());
        return result;
    }

    public static ResultActions putForbiddenWJWT(MockMvc mockMvc, String path, String jwt) throws Exception {
        ResultActions result = putWJWT(mockMvc, path, jwt);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isForbidden());
        return result;
    }

    public static ResultActions putForbidden(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        ResultActions result = put(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isForbidden());
        return result;
    }

    public static ResultActions putSuccessWJWT(MockMvc mockMvc, String path, String jwt) throws Exception {
        ResultActions result = putWJWT(mockMvc, path, jwt);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
        return result;
    }

    public static ResultActions putSuccess(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        ResultActions result = put(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
        return result;
    }

    public static ResultActions post(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(path)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(item));
    }

    public static ResultActions get(MockMvc mockMvc, String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(path));
    }

    public static ResultActions get(MockMvc mockMvc, String path, String jwt) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(path)
                .header("Authorization", "Bearer " + jwt));
    }

    public static ResultActions post(MockMvc mockMvc, String path, String item) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(item));
    }

    public static void createForbidden(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        ResultActions result = post(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isForbidden());
    }

    public static void createBadRequest(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        ResultActions result = post(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest());
    }

    public static void create(MockMvc mockMvc, String path, String item) throws Exception {
        ResultActions result = post(mockMvc, path, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isCreated());
    }

    public static void createOrFound(MockMvc mockMvc, String path, String item) throws Exception {
        ResultActions result = post(mockMvc, path, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .is(AnyOfStatusMatcher.createdOrFound()));
    }

    public static void create(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        ResultActions result = post(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isCreated());
    }

    public static void createOrFound(MockMvc mockMvc, String path, String jwt, String item) throws Exception {
        ResultActions result = post(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .is(AnyOfStatusMatcher.createdOrFound()));
    }

    public static ResultActions createOrFoundWithResult(MockMvc mockMvc, String path, String item)
            throws Exception {
        ResultActions result = post(mockMvc, path, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .is(AnyOfStatusMatcher.createdOrFound()));
        return result;
    }

    public static ResultActions createOrFoundWithResult(MockMvc mockMvc, String path, String jwt, String item)
            throws Exception {
        ResultActions result = post(mockMvc, path, jwt, item);
        result
                .andExpect(MockMvcResultMatchers.status()
                        .is(AnyOfStatusMatcher.createdOrFound()));
        return result;
    }
}
