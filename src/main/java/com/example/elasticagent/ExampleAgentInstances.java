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
import com.thoughtworks.go.plugin.api.logging.Logger;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesMonitoringEnabled;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.Instance;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ExampleAgentInstances implements AgentInstances<ExampleInstance> {

	//TODO: make everything threadsafe
	public static final Logger LOG = Logger.getLoggerFor(ExamplePlugin.class);
	
    private final ConcurrentHashMap<String, ExampleInstance> instances = new ConcurrentHashMap<>();

    private boolean refreshed;
    public Clock clock = Clock.DEFAULT;


    
    @Override
    public ExampleInstance create(CreateAgentRequest request, PluginSettings settings) throws Exception {
        // TODO: Implement me!
    	LOG.info("MyPlugin: create for jobIdentifier: " + request.jobIdentifier());
    	
    	AWSInstance newInstance = AWSInstance.Factory(request, settings);

    	//TODO: add instances to this thing ConcurrentHashMap<String, ExampleInstance> instances = new ConcurrentHashMap<>();
    	instances.put(newInstance.name(), newInstance);
    	
    	return newInstance;
    			/*
                TagSpecifications=[{
                    'ResourceType':'instance',
                    'Tags': [{
                        'Key': 'StopAt',
                        'Value': stopTime.isoformat()
                    }]
                }])*/
    	
    	
        //throw new UnsupportedOperationException();
//        ExampleInstance instance = ExampleInstance.create(request, settings);
//        register(instance);
//        return instance;
    }

    @Override
    public void terminate(String agentId, PluginSettings settings) throws Exception {
        // TODO: Implement me!
    	LOG.info("MyPlugin: terminate");
        throw new UnsupportedOperationException();

//        ExampleInstance instance = instances.get(agentId);
//        if (instance != null) {
//            instance.terminate(docker(settings));
//        } else {
//            LOG.warn("Requested to terminate an instance that does not exist " + agentId);
//        }
//        instances.remove(agentId);
    }

    @Override
    public void terminateUnregisteredInstances(PluginSettings settings, Agents agents) throws Exception {
        // TODO: Implement me!
    	LOG.info("MyPlugin: terminateUnregisteredInstances");
        //throw new UnsupportedOperationException();

//        ExampleAgentInstances toTerminate = unregisteredAfterTimeout(settings, agents);
//        if (toTerminate.instances.isEmpty()) {
//            return;
//        }
//
//        LOG.warn("Terminating instances that did not register " + toTerminate.instances.keySet());
//        for (ExampleInstance container : toTerminate.instances.values()) {
//            terminate(container.name(), settings);
//        }
    }

    @Override
    // TODO: Implement me!
    public Agents instancesCreatedAfterTimeout(PluginSettings settings, Agents agents) {
    	LOG.info("MyPlugin: instancesCreatedAfterTimeout");
        ArrayList<Agent> oldAgents = new ArrayList<>();
        for (Agent agent : agents.agents()) {
        	LOG.info("MyPlugin: looking for " + agent.elasticAgentId());
            ExampleInstance instance = instances.get(agent.elasticAgentId());
            if (instance == null) {
            	LOG.info("MyPlugin: instance not found in ConcurrentHashMap");
                continue;
            }

            if (clock.now().isAfter(instance.createdAt().plus(settings.getAutoRegisterPeriod()))) {
            	LOG.info("MyPlugin: agent is old");
                oldAgents.add(agent);
            }
        }
        LOG.info("MyPlugin: instancesCreatedAfterTimeout return");
        return new Agents(oldAgents);
    }

    @Override
    public void refreshAll(PluginRequest pluginRequest) throws Exception {
    	LOG.info("MyPlugin: refreshAll");
    	refreshed = true;
        // TODO: Implement me!
        //throw new UnsupportedOperationException();

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
    	LOG.info("MyPlugin: find agentID");
        return instances.get(agentId);
    }

    @Override
    public ExampleInstance find(JobIdentifier jobIdentifier) {
    	LOG.info("MyPlugin: find jobId");
        // TODO: Implement me!
//        return instances.values()
//                .stream()
//                .filter(x -> x.jobIdentifier().equals(jobIdentifier))
//                .findFirst()
//                .orElse(null);
        throw new UnsupportedOperationException();
    }

    @Override
    public StatusReport getStatusReport(PluginSettings pluginSettings) throws Exception {
    	LOG.info("MyPlugin: getStatusReport");
        // TODO: Implement me!
        // TODO: Read status information about agent instances from the cloud provider
//        return new StatusReport("")
        throw new UnsupportedOperationException();
    }

    @Override
    public AgentStatusReport getAgentStatusReport(PluginSettings pluginSettings, ExampleInstance agentInstance) {
    	LOG.info("MyPlugin: getAgentStatusReport");
        // TODO: Implement me!
        // TODO: Read status information about agent instance from the cloud provider
//        return new AgentStatusReport(null, null, null)
        throw new UnsupportedOperationException();
    }

    // used by tests
    public boolean hasInstance(String agentId) {
    	LOG.info("MyPlugin: hasInstance");
        return instances.containsKey(agentId);
    }

    private void register(ExampleInstance instance) {
    	LOG.info("MyPlugin: register");
        instances.put(instance.name(), instance);
    }

//    private ExampleAgentInstances unregisteredAfterTimeout(PluginSettings settings, Agents knownAgents) throws Exception {
//        Period period = settings.getAutoRegisterPeriod();
//        ExampleAgentInstances unregisteredContainers = new ExampleAgentInstances();
//
//        for (String instanceName : instances.keySet()) {
//            if (knownAgents.containsAgentWithId(instanceName)) {
//                continue;
//            }
//
//            // TODO: Connect to the cloud provider to fetch information about this instance
//            InstanceInfo instanceInfo = connection.inspectInstance(instanceName);
//            DateTime dateTimeCreated = new DateTime(instanceInfo.created());
//
//            if (clock.now().isAfter(dateTimeCreated.plus(period))) {
//                unregisteredContainers.register(ExampleInstance.fromInstanceInfo(instanceInfo));
//            }
//        }
//        return unregisteredContainers;
//    }
}
