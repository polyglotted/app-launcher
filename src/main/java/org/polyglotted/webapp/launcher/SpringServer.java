package org.polyglotted.webapp.launcher;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringServer implements Server, Runnable {

    private static final String APP_XML = "/META-INF/spring/main-context.xml";

    private AbstractApplicationContext applicationContext;
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void start() throws Exception {
        final Thread runnerThread = new Thread(this);
        runnerThread.setDaemon(true);
        runnerThread.setName("main.runner.thread");
        runnerThread.start();
        latch.await();
    }

    @Override
    public void run() {
        applicationContext = new ClassPathXmlApplicationContext(APP_XML);
        applicationContext.start();
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
        latch.countDown();
    }
}
