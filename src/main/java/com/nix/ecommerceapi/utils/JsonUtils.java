package com.nix.ecommerceapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nix.ecommerceapi.exception.AppException;

public class JsonUtils {
    public static String objectToJsonString(Object o) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new AppException("Cannot convert to json");
        }
    }
}
