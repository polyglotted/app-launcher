package io.polyglotted.applauncher.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ServerShutdownHook extends Thread {
    private final Server server;

    @Override
    public void run() {
        log.info("Shutting down server");
        server.stop();
    }
}
