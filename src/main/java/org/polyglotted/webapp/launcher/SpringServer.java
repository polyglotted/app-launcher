package org.polyglotted.webapp.launcher;

import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.SneakyThrows;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringServer implements Server {

    private static final String APP_XML = "/META-INF/spring/main-context.xml";

    @Getter
    private AbstractApplicationContext applicationContext;
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void start() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(APP_XML);
        applicationContext.start();
        new Thread(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                latch.await();
            }
        }).start();
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
        latch.countDown();
    }
}
