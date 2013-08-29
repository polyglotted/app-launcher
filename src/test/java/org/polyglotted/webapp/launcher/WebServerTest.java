package org.polyglotted.webapp.launcher;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.webapp.launcher.LaunchStrategy.WebApp;
import static org.polyglotted.webapp.launcher.util.TestUtils.asStream;

import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Test;

import com.google.common.io.CharStreams;

public class WebServerTest {

    @Test
    public void testWebServer() throws Exception {
        WebServer server = (WebServer) WebApp.create();
        server.start();
        URL url = new URL("http://localhost:8080/");
        String result = CharStreams.toString(new InputStreamReader(url.openStream()));
        String expected = CharStreams.toString(new InputStreamReader(asStream("webapp/index.html")));
        assertEquals(expected, result);
        server.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testWebServerFail() throws Exception {
        System.setProperty("webapp.in.ide", "true");
        WebServer server = (WebServer) WebApp.create();
        try {
            server.start();
        }
        finally {
            server.stop();
            System.clearProperty("webapp.in.ide");
        }
    }

    @Test
    public void testEnsureLocalFileExists() {
        String path = "src/test/resources";
        assertEquals(path, WebServer.ensureLocalFileExists(path));
    }
}
