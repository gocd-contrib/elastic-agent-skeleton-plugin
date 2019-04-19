package com.example.elasticagent.executors;

import com.example.elasticagent.RequestExecutor;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.ArrayList;
import java.util.List;

import static com.example.elasticagent.PluginSettings.GSON;

public class GetClusterProfileMetadataExecutor implements RequestExecutor {
    public static final Metadata GO_SERVER_URL = new Metadata("go_server_url", "Go Server URL", true, false);
    public static final Metadata API_USER = new Metadata("api_user", "API User", true, false);
    public static final Metadata API_KEY = new Metadata("api_key", "API Key", true, false);
    public static final Metadata API_URL = new Metadata("api_url", "API URL", true, false);
    public static final Metadata AUTO_REGISTER_TIMEOUT = new NumberMetadata("auto_register_timeout", "Agent auto-register Timeout (in minutes)", true);


    public static final List<Metadata> CLUSTER_PROFILE_FIELDS = new ArrayList<>();

    static {
        CLUSTER_PROFILE_FIELDS.add(GO_SERVER_URL);
        CLUSTER_PROFILE_FIELDS.add(API_USER);
        CLUSTER_PROFILE_FIELDS.add(API_KEY);
        CLUSTER_PROFILE_FIELDS.add(API_URL);
        CLUSTER_PROFILE_FIELDS.add(AUTO_REGISTER_TIMEOUT);
    }

    @Override

    public GoPluginApiResponse execute() throws Exception {
        return new DefaultGoPluginApiResponse(200, GSON.toJson(CLUSTER_PROFILE_FIELDS));
    }
}