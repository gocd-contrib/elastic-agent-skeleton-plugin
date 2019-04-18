package com.example.elasticagent.requests;

import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.executors.ClusterProfileValidateRequestExecutor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import static com.example.elasticagent.requests.ShouldAssignWorkRequest.GSON;

public class ClusterProfileValidateRequest {
    private Map<String, String> properties;

    public ClusterProfileValidateRequest(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public static ClusterProfileValidateRequest fromJSON(String json) {
        final Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        final Map<String, String> properties = GSON.fromJson(json, type);
        return new ClusterProfileValidateRequest(properties);
    }

    public RequestExecutor executor() {
        return new ClusterProfileValidateRequestExecutor(this);
    }
}
