package com.kt.dotcreator.store;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonHandler {
    private static ObjectMapper mapper = new ObjectMapper();

    public static ProjectData decodeProject(String json) throws IOException{
        return mapper.readValue(json, ProjectData.class);
    }

    public static String encodeProject(ProjectData projectData) throws IOException{
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(projectData);
    }
}
