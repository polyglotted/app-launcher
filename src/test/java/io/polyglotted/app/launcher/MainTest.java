package io.polyglotted.app.launcher;

import org.junit.Test;
import io.polyglotted.app.core.Gaveti;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static io.polyglotted.app.core.Overrides.value;

public class MainTest extends Main {

    @Test
    public void startFromConfig() {
        Starter.reset();

        SpringServer server = Main.main(randomGaveti().asConfig());
        checkAndStop(server, "");
    }

    @Test
    public void startServerFromDifferentConfig() {
        Starter.reset();

        SpringServer server = Main.main(randomGaveti(), value("aProperty", "overriddenValue"),
                new Class[]{DifferentConfiguration.class});
        checkAndStop(server, "overriddenValue");
    }

    private void checkAndStop(SpringServer server, String value) {
        assertNotNull(server.getApplicationContext());
        assertTrue(Starter.started);
        assertThat(Starter.artifactId, is("bar"));
        assertThat(Starter.aProperty, is(value));

        server.stop();
        assertFalse(Starter.started);
    }

    private Gaveti randomGaveti() {
        return new Gaveti("foo", "bar", "1.0.0", "baz");
    }
}
