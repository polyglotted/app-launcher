package io.polyglotted.app.core;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

public abstract class Ports {

    public static int random() {
        return allocate(Optional.<Integer>empty());
    }

    @SneakyThrows(IOException.class)
    public static int allocate(Optional<Integer> port) {
        try (ServerSocket socket = new ServerSocket(port.orElse(0))) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }
}
