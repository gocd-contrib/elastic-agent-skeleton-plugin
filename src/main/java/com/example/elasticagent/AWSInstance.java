package com.example.elasticagent;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.example.elasticagent.executors.AgentProfileField;
import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.requests.CreateAgentRequest;
import com.thoughtworks.go.plugin.api.logging.Logger;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.ResourceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesMonitoringEnabled;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.TagSpecification;
import software.amazon.awssdk.services.ec2.model.TagSpecification.Builder;

//TODO: clean up this file
public class AWSInstance extends ExampleInstance{

	private static Ec2Client ec2 = null;
	
	public static final Logger LOG = Logger.getLoggerFor(AWSInstance.class);
	public static Clock clock = Clock.DEFAULT;
	
    private static String addNewline(String str, String newLine)
    {
    	str = str + newLine + "\r\n";
    	return str;
    }
	
	public static AWSInstance Factory(CreateAgentRequest request, PluginSettings settings) throws Exception {
		if(ec2 == null) {
			ec2 = Ec2Client.create();
		}
		LOG.info("CreateAgentRequest JSON: " + request.toJson());
	
		//TODO: build userdata in instanceBuilder
    	String userData = "";
    	userData = addNewline(userData, "<powershell>");
    	userData = addNewline(userData, "mkdir \"C:\\Program Files (x86)\\Go Agent\\config\"");
    	userData = addNewline(userData, "$instanceId = (Invoke-WebRequest http://169.254.169.254/latest/meta-data/instance-id).Content");
    	userData = addNewline(userData, "$hostName = (Invoke-WebRequest http://169.254.169.254/latest/meta-data/public-hostname).Content");	
    	userData = addNewline(userData, "$UserInfoToFile = @\"");
    	userData = addNewline(userData, request.autoregisterPropertiesAsString("$instanceId", "$hostName"));
    	userData = addNewline(userData, "\"@");
    	userData = addNewline(userData, "$UserInfoToFile | Out-File -FilePath \"C:\\Program Files (x86)\\Go Agent\\config\\autoregister.properties\" -Encoding ASCII");
    	userData = addNewline(userData, "Invoke-WebRequest -OutFile C:\\Users\\Administrator\\Downloads\\go-agent-18.11.0-8024-jre-64bit-setup.exe https://download.gocd.org/binaries/18.11.0-8024/win/go-agent-18.11.0-8024-jre-64bit-setup.exe");
    	userData = addNewline(userData, "C:\\Users\\Administrator\\Downloads\\go-agent-18.11.0-8024-jre-64bit-setup.exe /S /START_AGENT=YES /SERVERURL=`\"" + settings.getGoServerUrl() + "`\"");  //TODO: set server URL correctly
    	userData = addNewline(userData, "</powershell>");
    	userData = Base64.getEncoder().encodeToString(userData.getBytes());
    	
    	//TODO: build this with information from the request
    	AWSInstanceBuilder awsBuilder = new AWSInstanceBuilder();
    	for(AgentProfileField.Command command : request.getPropertyCommands())
		{
			command.apply(awsBuilder);
		}
    	awsBuilder.addInstanceTags(request.getTagsForInstance());
    	
    	RunInstancesRequest runInstancesRequest = awsBuilder.build(userData);
    	

    	
    	
    	
    	LOG.info("AWSInstance Factory: starting request");
    	RunInstancesResponse response = ec2.runInstances(runInstancesRequest);		
    	LOG.info("AWSInstance Factory: create: created " + response.instances().size() + " instances");
    	for (Instance instance : response.instances())
    	{
    		LOG.info("AWSInstance Factory: create: id: " + instance.instanceId());
    	}
    	
    	Instance newinstance = response.instances().get(0);
    	
    	AWSInstance newInstance = new AWSInstance(newinstance.instanceId(), clock.now().toDate(), request.properties(), request.environment(), request.jobIdentifier());  //TODO: tag ec2 instance to match this
    	LOG.info("AWSInstance Factory: return");
    	return newInstance;
	}
	
	public AWSInstance(String name, Date createdAt, Map<String, String> properties, String environment,
			JobIdentifier jobIdentifier) {
		super(name, createdAt, properties, environment, jobIdentifier);
		// TODO Auto-generated constructor stub
	}

}
