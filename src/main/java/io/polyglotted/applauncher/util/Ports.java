package io.polyglotted.applauncher.util;

import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.util.Optional;

public abstract class Ports {

    public static int randomPort() {
        return allocate(Optional.<Integer>empty());
    }

    @SneakyThrows
    public static int allocate(Optional<Integer> port) {
        try (ServerSocket socket = new ServerSocket(port.orElse(0))) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }
}
