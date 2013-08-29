package org.polyglotted.webapp.launcher;

import lombok.RequiredArgsConstructor;

public abstract class Main {

    public static void main(String... anArgs) throws Exception {
        final Server server = getLaunchStrategy().create();
        Runtime.getRuntime().addShutdownHook(new ServerShutdown(server));
        server.start();
    }

    static LaunchStrategy getLaunchStrategy() {
        try {
            return LaunchStrategy.valueOf(System.getProperty("main.launch.strategy", "WebApp"));
        }
        catch (Exception ex) {
            return LaunchStrategy.WebApp;
        }
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
