package org.polyglotted.webapp.launcher;

import lombok.SneakyThrows;

public class Main {

    public static void main(String... anArgs) throws Exception {
        final Server server = getLaunchStrategy().create();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                System.out.println("Shutting down server");
                server.stop();
            }
        });
        server.start();
    }

    private static LaunchStrategy getLaunchStrategy() {
        try {
            return LaunchStrategy.valueOf(System.getProperty("main.launch.strategy", "WebApp"));
        }
        catch (Exception ex) {
            return LaunchStrategy.WebApp;
        }
    }
}
