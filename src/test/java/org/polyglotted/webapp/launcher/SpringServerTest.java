package org.polyglotted.webapp.launcher;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.webapp.launcher.LaunchStrategy.SpringApp;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class SpringServerTest {

    @Test
    public void testSpringServer() throws Exception {
        SpringServer server = (SpringServer) SpringApp.create();
        server.start();
        ApplicationContext ctx = server.getApplicationContext();
        @SuppressWarnings("unchecked")
        List<String> people = (List<String>) ctx.getBean("people");
        assertEquals("[shankar, david, meena]", people.toString());
        server.stop();
    }
}
