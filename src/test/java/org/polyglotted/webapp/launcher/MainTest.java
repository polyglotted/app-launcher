package org.polyglotted.webapp.launcher;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class MainTest extends Main {
    @Mocked Server mockServer;

    @Test
    public void testGetLaunchStrategy() throws Exception {
        System.setProperty("main.launch.strategy", "SomeApp");
        assertEquals(LaunchStrategy.WebApp, Main.getLaunchStrategy());
        System.clearProperty("main.launch.strategy");
    }

    @Test
    public void testGetLaunchStrategyDefault() throws Exception {
        assertEquals(LaunchStrategy.WebApp, Main.getLaunchStrategy());
    }

    @Test
    public void testServerShutdown() throws Exception {
        new Expectations() {
            {
                mockServer.stop();
            }
        };
        new Main.ServerShutdown(mockServer).run();
    }

    @Test
    public void testMain() throws Exception {
        try {
            System.setProperty("jetty.http.port", "13090");
            Main.main(new String[0]);
        }
        finally {
            System.clearProperty("jetty.http.port");
        }
    }
}
