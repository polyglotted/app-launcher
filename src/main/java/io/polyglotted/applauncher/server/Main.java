package io.polyglotted.applauncher.server;

import lombok.SneakyThrows;

public abstract class Main {

    public static final String SERVER_CLASS = "applauncher.server.class";

    @SneakyThrows
    public static void main(String[] args) {
        String serverClazz = System.getProperty(SERVER_CLASS, SpringServer.class.getCanonicalName());
        main((Server) Class.forName(serverClazz).newInstance());
    }

    public static void main(Server server) {
        Runtime.getRuntime().addShutdownHook(new ServerShutdownHook(server));
        server.start();
    }

}
