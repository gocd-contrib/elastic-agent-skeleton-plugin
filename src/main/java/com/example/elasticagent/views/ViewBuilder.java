package com.example.elasticagent.views;

import com.example.elasticagent.ExampleInstance;
import com.example.elasticagent.models.StatusReport;
import com.google.common.html.HtmlEscapers;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.stream.Collectors;

//TODO You can modify and use this class to generate views
@SuppressWarnings("UnstableApiUsage")
public class ViewBuilder {

    private static ViewBuilder builder;

    public static ViewBuilder instance() {
        if (builder == null) {
            builder = new ViewBuilder();
        }
        return builder;
    }

    public String build(String template) {
        return "";
    }

    public String build(String template, Object model) {
        return "";
    }

    public String build(String template, StatusReport statusReport) {
        // TODO implement me!

        return headingFor(statusReport) + instancesListing(statusReport.getInstances());
    }

    private static String headingFor(StatusReport statusReport) {
        return String.format("<h3>Status for cluster profile %s:</h3>", statusReport.getClusterProfileUuid());
    }

    private String instancesListing(Map<String, ExampleInstance> instances) {
        return instances.entrySet().stream()
                .map(blah -> titleView(blah.getKey()) + instanceView(blah.getValue()))
                .collect(Collectors.joining("<br/>"));
    }

    private String titleView(String instanceId) {
        return String.format("<div>ID: %s</div>",
                HtmlEscapers.htmlEscaper().escape(instanceId));
    }

    private String instanceView(ExampleInstance instance) {
        return String.format("<div>Name: %s</div><div>Created At: %s</div>",
                HtmlEscapers.htmlEscaper().escape(instance.name()),
                toViewRenderedTime(instance.createdAt()));
    }

    private String toViewRenderedTime(DateTime dateTime) {
        // Render something using Angular!
        return HtmlEscapers.htmlEscaper().escape(
                String.format("{{ %s | date:'MMM dd, yyyy HH:mm Z' }}", dateTime.getMillis())
        );
    }
}
