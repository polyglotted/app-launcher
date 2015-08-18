package io.polyglotted.applauncher.util;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class PortsTest extends Ports {

    @Test
    public void testEmpty() {
        assertThat(randomPort(), is(greaterThan(0)));
    }
}