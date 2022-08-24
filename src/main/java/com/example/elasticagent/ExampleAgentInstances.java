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

import com.example.elasticagent.models.AgentStatusReport;
import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.models.StatusReport;
import com.example.elasticagent.requests.CreateAgentRequest;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.elasticagent.ExamplePlugin.LOG;


//TODO: This is just a basic implementation, all of the following methods need to be implemented
public class ExampleAgentInstances implements AgentInstances<ExampleInstance> {

    private final ConcurrentHashMap<String, ExampleInstance> instances = new ConcurrentHashMap<>();

    private boolean refreshed;
    public Clock clock = Clock.DEFAULT;

    @Override
    public ExampleInstance create(CreateAgentRequest request) throws Exception {
        // TODO: Implement me!
        ExampleInstance instance = ExampleInstance.create(request);
        register(instance);
        return instance;
    }

    @Override
    public void terminate(String agentId, ClusterProfileProperties clusterProfile) throws Exception {
        // TODO: Implement me!

        ExampleInstance instance = instances.get(agentId);
        if (instance != null) {
            // terminate instance
            //instance.terminate(docker(settings));
        } else {
            LOG.warn("Requested to terminate an instance that does not exist " + agentId);
        }
        instances.remove(agentId);
    }

    @Override
    public void terminateUnregisteredInstances(ClusterProfileProperties clusterProfile, Agents agents) throws Exception {
        // TODO: Implement me!

        ExampleAgentInstances toTerminate = unregisteredAfterTimeout(clusterProfile, agents);
        if (toTerminate.instances.isEmpty()) {
            return;
        }

        LOG.warn("Terminating instances that did not register " + toTerminate.instances.keySet());
        for (ExampleInstance container : toTerminate.instances.values()) {
            terminate(container.name(), clusterProfile);
        }
    }

    @Override
    // TODO: Implement me!
    public Agents instancesCreatedAfterTimeout(ClusterProfileProperties clusterProfile, Agents agents) {
        ArrayList<Agent> oldAgents = new ArrayList<>();
        for (Agent agent : agents.agents()) {
            ExampleInstance instance = instances.get(agent.elasticAgentId());
            if (instance == null) {
                continue;
            }

            if (clock.now().isAfter(instance.createdAt().plus(clusterProfile.getAutoRegisterPeriod()))) {
                oldAgents.add(agent);
            }
        }
        return new Agents(oldAgents);
    }

    @Override
    public void refreshAll(ClusterProfileProperties clusterProfileProperties) throws Exception {
        // TODO: Implement me!

//        if (!refreshed) {
//            TODO: List all instances from the cloud provider and select the ones that are created by this plugin
//            TODO: Most cloud providers allow applying some sort of labels or tags to instances that you may find of use
//            List<InstanceInfo> instanceInfos = cloud.listInstances().filter(...)
//            for (Instance instanceInfo: instanceInfos) {
//                  register(ExampleInstance.fromInstanceInfo(instanceInfo))
//            }
//            refreshed = true;
//        }
    }

    @Override
    public ExampleInstance find(String agentId) {
        return instances.get(agentId);
    }

    @Override
    public ExampleInstance find(JobIdentifier jobIdentifier) {
        // TODO: Implement me!
//        return instances.values()
//                .stream()
//                .filter(x -> x.jobIdentifier().equals(jobIdentifier))
//                .findFirst()
//                .orElse(null);
        throw new UnsupportedOperationException();
    }

    @Override
    public StatusReport getStatusReport(ClusterProfileProperties clusterProfileProperties) throws Exception {
        // TODO: Implement me!

        // Add dummy instances
        if (instances.isEmpty()) {
            Date createdAt = new Date(ZonedDateTime.of(2022, 8, 27, 12, 12, 12, 12, ZoneId.of("UTC")).toInstant().toEpochMilli());
            instances.put("instance-1", new ExampleInstance("instance-1", createdAt, Collections.emptyMap(), "", null));
            instances.put("instance-2", new ExampleInstance("instance-2", createdAt, Collections.emptyMap(), "", null));
        }

        return new StatusReport(clusterProfileProperties, instances);
    }

    @Override
    public AgentStatusReport getAgentStatusReport(ClusterProfileProperties pluginSettings, ExampleInstance agentInstance) {
        // TODO: Implement me!
        // TODO: Read status information about agent instance from the cloud provider
//        return new AgentStatusReport(null, null, null)
        throw new UnsupportedOperationException();
    }

    // used by tests
    public boolean hasInstance(String agentId) {
        return instances.containsKey(agentId);
    }

    private void register(ExampleInstance instance) {
        instances.put(instance.name(), instance);
    }

    private ExampleAgentInstances unregisteredAfterTimeout(ClusterProfileProperties settings, Agents knownAgents) throws Exception {
        Period period = settings.getAutoRegisterPeriod();
        ExampleAgentInstances unregisteredContainers = new ExampleAgentInstances();

        for (String instanceName : instances.keySet()) {
            if (knownAgents.containsAgentWithId(instanceName)) {
                continue;
            }

            // TODO: Connect to the cloud provider to fetch information about this instance
            // InstanceInfo instanceInfo = connection.inspectInstance(instanceName);
            DateTime dateTimeCreated = new DateTime();

            if (clock.now().isAfter(dateTimeCreated.plus(period))) {
                unregisteredContainers.register(new ExampleInstance(instanceName, dateTimeCreated.toDate(), null, null, null));
            }
        }
        return unregisteredContainers;
    }
}
