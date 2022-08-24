package com.example.elasticagent.models;

import com.example.elasticagent.ClusterProfileProperties;
import com.example.elasticagent.ExampleInstance;

import java.util.Map;

public class StatusReport {

    private final String clusterProfileUuid;
    private final Map<String, ExampleInstance> instances;
    //Add fields as needed

    public StatusReport(ClusterProfileProperties clusterProfileProperties, Map<String, ExampleInstance> instances) {
        clusterProfileUuid = clusterProfileProperties.uuid();
        this.instances = instances;
    }

    public Map<String, ExampleInstance> getInstances() {
        return instances;
    }

    public String getClusterProfileUuid() {
        return clusterProfileUuid;
    }
}
