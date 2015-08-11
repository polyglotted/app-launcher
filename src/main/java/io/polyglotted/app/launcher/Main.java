package io.polyglotted.app.launcher;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import io.polyglotted.app.core.Gaveti;
import io.polyglotted.app.core.Overrides;

public abstract class Main {

    public static void main(String... anArgs) {
        main(ConfigFactory.systemProperties());
    }

    public static Server main(Config config) {
        return main(Gaveti.from(config));
    }

    public static Server main(Gaveti gaveti) {
        return main(gaveti, Overrides.empty());
    }

    @SneakyThrows
    public static Server main(Gaveti gaveti, Overrides overrides, Class<?>... configClasses) {
        Class[] configurationClasses = (configClasses.length == 0) ? SpringServer.defaultConfigurationClasses() : configClasses;
        final SpringServer server = new SpringServer(gaveti, overrides, configurationClasses);
        Runtime.getRuntime().addShutdownHook(new ServerShutdown(server));
        server.start();
        return server;
    }

    @RequiredArgsConstructor
    static class ServerShutdown extends Thread {
        private final Server serverApp;

        @Override
        public void run() {
            System.out.println("Shutting down server");
            serverApp.stop();
        }
    }
}
