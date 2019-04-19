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

import org.joda.time.Period;

import java.util.Map;

// TODO: Implement any settings that your plugin needs
public class ClusterProfile extends PluginSettings{
    public ClusterProfile() {
    }

    public ClusterProfile(String goServerUrl, String autoRegisterTimeout, String apiUser, String apiKey, String apiUrl, Period autoRegisterPeriod) {
        super(goServerUrl, autoRegisterTimeout, apiUser, apiKey, apiUrl, autoRegisterPeriod);
    }

    public static ClusterProfile fromJSON(String json) {
        return GSON.fromJson(json, ClusterProfile.class);
    }

    public static ClusterProfile fromConfiguration(Map<String, String> clusterProfile) {
        return GSON.fromJson(GSON.toJson(clusterProfile), ClusterProfile.class);
    }

    public String uuid() {
        return Integer.toHexString(hashCode());
    }
}
