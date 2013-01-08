package org.polyglotted.webapp.launcher;

public class Main {

    public static void main(String... anArgs) throws Exception {

        final WebServer server = new WebServer();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("Shutting down webserver");
                    server.stop();
                } catch (Exception e) {
                }
            }
        });
        server.start();
        server.join();
    }
}
