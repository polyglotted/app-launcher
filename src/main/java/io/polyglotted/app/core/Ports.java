package io.polyglotted.app.core;

import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.util.Optional;

abstract public class Ports {

    @SneakyThrows
    public static int allocate(Optional<Integer> port) {
        try (ServerSocket socket = new ServerSocket(port.orElse(0))) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }

}
