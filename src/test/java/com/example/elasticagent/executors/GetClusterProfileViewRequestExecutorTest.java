package com.example.elasticagent.executors;

import com.example.elasticagent.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.containsString;


import java.lang.reflect.Type;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetClusterProfileViewRequestExecutorTest {

    public static final String CLUSTER_PROFILE_TEMPLATE = "/cluster-profile.template.html";

    @Test
    public void shouldRenderTheTemplateInJSON() throws Exception {
        GoPluginApiResponse response = new GetClusterProfileViewRequestExecutor().execute();
        assertThat(response.responseCode(), is(200));
        final Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> hashSet = new Gson().fromJson(response.responseBody(), type);
        assertThat(hashSet, hasEntry("template", Util.readResource(CLUSTER_PROFILE_TEMPLATE)));
    }

    @Test
    public void allFieldsShouldBePresentInView() throws Exception {
        String template = Util.readResource(CLUSTER_PROFILE_TEMPLATE);

        for (Metadata field : GetClusterProfileMetadataExecutor.CLUSTER_PROFILE_FIELDS) {
            assertThat(template, containsString("ng-model=\"" + field.getKey() + "\""));
            assertThat(template, containsString("<span class=\"form_error\" ng-show=\"GOINPUTNAME[" + field.getKey() +
                    "].$error.server\">{{GOINPUTNAME[" + field.getKey() +
                    "].$error.server}}</span>"));
        }
    }
}