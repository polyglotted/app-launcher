package org.polyglotted.webapp.launcher;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.webapp.launcher.LaunchStrategy.SpringApp;

import java.util.List;

import lombok.SneakyThrows;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class SpringServerTest {

    private SpringServer server = (SpringServer) SpringApp.create();

    @Before
    public void initTest() throws Exception {
        server.start();
    }

    @After
    public void tearTest() throws Exception {
        server.stop();
    }

    @Test
    public void testMyClass() {
        ApplicationContext ctx = server.getApplicationContext();
        @SuppressWarnings("unchecked") 
        List<String> people = (List<String>) ctx.getBean("people");
        assertEquals("[shankar, david, meena]", people.toString());
    }

    public static void main(String... anArgs) throws Exception {
        final SpringServer server = new SpringServer();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                server.stop();
            }
        });
        server.start();
    }
}
