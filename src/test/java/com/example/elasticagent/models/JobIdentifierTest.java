package com.example.elasticagent.models;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JobIdentifierTest {

    @Test
    public void shouldDeserializeFromJson() {
        JobIdentifier jobIdentifier = JobIdentifier.fromJson(JobIdentifierMother.getJson().toString());

        JobIdentifier expected = JobIdentifierMother.get();
        assertThat(jobIdentifier, is(expected));
    }

    @Test
    public void shouldGetRepresentation() {
        String representation = JobIdentifierMother.get().getRepresentation();

        assertThat(representation, is("up42/1/stage/1/job1"));
    }
}
