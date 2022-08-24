/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.elasticagent;

import com.google.gson.annotations.Expose;

import java.util.Objects;

import static com.example.elasticagent.ExamplePlugin.GSON;

// TODO: Implement any settings that your plugin needs
public class ClusterProfile {
    @Expose
    private final String id;

    @Expose
    private final String pluginId;

    @Expose
    private final ClusterProfileProperties properties;

    public ClusterProfile() {
        this("", "", new ClusterProfileProperties());
    }

    public ClusterProfile(String id, String pluginId, ClusterProfileProperties clusterProfileProperties) {
        this.id = id;
        this.pluginId = pluginId;
        this.properties = clusterProfileProperties;
    }

    public static ClusterProfile fromJSON(String json) {
        return GSON.fromJson(json, ClusterProfile.class);
    }

    public String getId() {
        return id;
    }

    public String getPluginId() {
        return pluginId;
    }

    public ClusterProfileProperties getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusterProfile that = (ClusterProfile) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (pluginId != null ? !pluginId.equals(that.pluginId) : that.pluginId != null) return false;
        return properties != null ? properties.equals(that.properties) : that.properties == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pluginId, properties);
    }
}
