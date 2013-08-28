package org.polyglotted.webapp.launcher;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringServer implements Server {

    private static final String APP_XML = "/META-INF/spring/main-context.xml";

    private AbstractApplicationContext applicationContext;

    @Override
    public void start() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(APP_XML);
        applicationContext.start();
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
    }
}
