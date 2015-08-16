package io.polyglotted.app.core;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class PortsTest extends Ports {

    @Test
    public void testEmpty() {
        assertThat(random(), is(greaterThan(0)));
    }
}