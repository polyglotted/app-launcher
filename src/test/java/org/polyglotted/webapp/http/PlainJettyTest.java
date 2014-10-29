package org.polyglotted.webapp.http;


import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Test;
import org.polyglotted.webapp.http.impl.PlainJetty;
import org.polyglotted.webapp.resources.impl.DefaultPropertiesFactory;
import org.polyglotted.webapp.resources.impl.MapValuesProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.polyglotted.webapp.http.impl.PlainJetty.generatePort;

public class PlainJettyTest {

    @Test
    public void jetty_starts_and_serves_a_page() throws Exception {
        int port = generatePort();
        final JettyConfig config = new DefaultPropertiesFactory(
                MapValuesProvider.forSource("jetty.properties",
                        ImmutableMap.<String, Object>builder()
                                .put(JettyConfig.JettyHttpBindInterface, "localhost")
                                .put(JettyConfig.JettyHttpPort, port)
                                .put(JettyConfig.JettyHttpMinThreads, 1)
                                .put(JettyConfig.JettyHttpMaxThreads, 100)
                                .build())
        ).properties(JettyConfig.class);
        final Server server = new PlainJetty(config, Arrays.<Handler>asList(helloHandler())).createServer();
        server.start();
        final HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + port).openConnection();
        final String response = CharStreams.toString(new InputStreamReader(connection.getInputStream()));
        assertThat(response, is("Hello"));
        server.stop();
    }

    private AbstractHandler helloHandler() {
        return new AbstractHandler() {
            @Override
            public void handle(String target,
                               Request baseRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException, ServletException {
                final PrintWriter writer = response.getWriter();
                writer.write("Hello");
                writer.close();
            }
        };
    }
}
