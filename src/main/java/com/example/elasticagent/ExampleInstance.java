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

import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.requests.CreateAgentRequest;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ExampleInstance {
    private final DateTime createdAt;
    private final Map<String, String> properties;
    private final String environment;
    private final JobIdentifier jobIdentifier;
    private String name;

    public ExampleInstance(String name, Date createdAt, Map<String, String> properties, String environment, JobIdentifier jobIdentifier) {
        this.name = name;
        this.createdAt = new DateTime(createdAt);
        this.properties = properties;
        this.environment = environment;
        this.jobIdentifier = jobIdentifier;
    }

    public static ExampleInstance create(CreateAgentRequest request) {
        return new ExampleInstance("agent_" + UUID.randomUUID().toString(), new Date(), request.profileProperties(), request.environment(), request.jobIdentifier());
    }

    public String name() {
        return name;
    }

    public DateTime createdAt() {
        return createdAt;
    }

    public String environment() {
        return environment;
    }

    public Map<String, String> properties() {
        return properties;
    }

    public JobIdentifier jobIdentifier() {
        return jobIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExampleInstance that = (ExampleInstance) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
