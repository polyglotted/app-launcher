package org.polyglotted.webapp.launcher;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.polyglotted.webapp.core.Gaveti;
import org.polyglotted.webapp.core.Overrides;

import static org.polyglotted.webapp.launcher.SpringServer.defaultConfigurationClasses;

public abstract class Main {

    public static void main(String... anArgs) {
        main(ConfigFactory.systemProperties());
    }

    public static SpringServer main(Config config) {
        return main(Gaveti.from(config));
    }

    public static SpringServer main(Gaveti gaveti) {
        return main(gaveti, Overrides.empty());
    }

    @SneakyThrows
    public static SpringServer main(Gaveti gaveti, Overrides overrides, Class<?>... configClasses) {
        Class[] configurationClasses = (configClasses.length == 0) ? defaultConfigurationClasses() : configClasses;
        final SpringServer server = new SpringServer(gaveti, overrides, configurationClasses);
        Runtime.getRuntime().addShutdownHook(new ServerShutdown(server));
        server.start();
        return server;
    }

    @RequiredArgsConstructor
    static class ServerShutdown extends Thread {
        private final SpringServer serverApp;

        @Override
        public void run() {
            System.out.println("Shutting down server");
            serverApp.stop();
        }
    }
}
