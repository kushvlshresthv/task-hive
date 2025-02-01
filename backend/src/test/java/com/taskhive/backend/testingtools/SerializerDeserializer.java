package com.taskhive.backend.testingtools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskhive.backend.response.Response;

public class SerializerDeserializer {

    public static Response deserialize(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Response deserializedResponse = mapper.readValue(response, Response.class);
            return deserializedResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
