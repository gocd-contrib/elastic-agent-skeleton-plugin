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

package com.example.elasticagent.executors;

import com.example.elasticagent.*;
import com.example.elasticagent.requests.MigrateConfigPayload;
import com.example.elasticagent.utils.Util;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Map;

import static com.example.elasticagent.PluginSettings.GSON;

public class MigrateConfigRequestExecutor implements RequestExecutor {
    private final MigrateConfigPayload payload;

    public MigrateConfigRequestExecutor(MigrateConfigPayload payload) {
        this.payload = payload;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        /*
         Derive a default cluster profile from plugin settings
          */

        PluginSettings pluginSettings = payload.pluginSettings();

        if (!payload.clusterProfiles().isEmpty() && "default".equals(payload.clusterProfiles().get(0).getId())) {
            return new DefaultGoPluginApiResponse(200, GSON.toJson(payload));
        }

        ClusterProfile clusterProfile = new ClusterProfile("default", Util.pluginId(),
                new ClusterProfileProperties(
                        pluginSettings.getGoServerUrl(),
                        String.valueOf(pluginSettings.getAutoRegisterPeriod().getMinutes()),
                        pluginSettings.getApiUser(),
                        pluginSettings.getApiKey(),
                        pluginSettings.getApiUrl(),
                        pluginSettings.getAutoRegisterPeriod()
                ));

        payload.clusterProfiles().add(clusterProfile);

        /*
         Link each profile with newly created cluster profile
          */

        for (Map<String, String> profile : payload.elasticAgentProfileProperties()) {
            profile.put("clusterProfileId", clusterProfile.getId());
        }

        return new DefaultGoPluginApiResponse(200, GSON.toJson(payload));
    }
}
