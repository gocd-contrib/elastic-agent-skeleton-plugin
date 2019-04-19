package com.example.elasticagent.executors;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NumberMetadataTest {

    @Test
    public void shouldErrorOutWhenValueIsRequiredAndBlank() {
        Metadata metadata = new NumberMetadata("number-meta", "Foo", true);
        Map<String, String> error = metadata.validate("");
        assertThat(error.keySet(), hasItems("key", "message"));
        assertThat(error.get("key"), is("number-meta"));
        assertThat(error.get("message"), is("Foo must be a positive integer."));
    }

    @Test
    public void shouldNotErrorOutWhenValueIsRequiredAndNotBlank() {
        Metadata metadata = new NumberMetadata("number-meta", "Foo", true);
        Map<String, String> error = metadata.validate("20");
        assertThat(error.size(), is(0));
    }

    @Test
    public void shouldErrorOutIfValueIsNotPositive() {
        Metadata metadata = new NumberMetadata("number-meta", "Foo", false);
        Map<String, String> error = metadata.validate("-20");
        assertThat(error.keySet(), hasItems("key", "message"));
        assertThat(error.get("key"), is("number-meta"));
        assertThat(error.get("message"), is("Foo must be a positive integer."));
    }

    @Test
    public void shouldNotErrorOutWhenValueIsNotRequiredAndBlank() {
        Metadata metadata = new NumberMetadata("number-meta", "Foo", false);
        Map<String, String> error = metadata.validate("");
        assertThat(error.size(), is(0));
    }
}