package io.polyglotted.applauncher.server;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;

@Slf4j
@RequiredArgsConstructor
public class SpringServer implements Server {
    private final Class<?>[] configurationClasses;

    private AnnotationConfigApplicationContext applicationContext;
    private final CountDownLatch latch = new CountDownLatch(1);

    public SpringServer () {
        this(defaultConfigurationClasses());
    }

    @Override
    public void start() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(configurationClasses);
        applicationContext.refresh();
        applicationContext.start();
        new Thread(this::awaitLatch).start();
        log.info("Server started");
    }

    @SneakyThrows
    private void awaitLatch() {
        latch.await();
    }

    @Override
    public void stop() {
        applicationContext.close();
        latch.countDown();
    }

    @SneakyThrows
    private static Class<?>[] defaultConfigurationClasses() {
        return new Class[]{Class.forName("applauncher.spring.MainConfiguration")};
    }
}