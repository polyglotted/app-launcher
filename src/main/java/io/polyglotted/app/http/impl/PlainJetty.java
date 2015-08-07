package io.polyglotted.app.http.impl;

import io.polyglotted.app.http.Jetty;
import io.polyglotted.app.http.JettyConfig;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class PlainJetty implements Jetty {
    private final JettyConfig config;
    private final List<Handler> handlers;

    @Override
    public Server createServer() {
        final ThreadPool threadPool = threadPool();
        final Server server = new Server(threadPool);

        server.manage(threadPool);
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.addConnector(serverConnector(server));
        server.setHandler(handlersContainer());
        server.setStopAtShutdown(true);
        return server;
    }

    private Handler handlersContainer() {
        HandlerCollection result = new HandlerCollection();
        result.setHandlers(handlers.toArray(new Handler[0]));
        return result;
    }

    private ServerConnector serverConnector(Server server) {
        HttpConfiguration httpConfiguration = new HttpConfiguration();
        HttpConnectionFactory http = new HttpConnectionFactory(httpConfiguration);
        ServerConnector httpConnector = new ServerConnector(server, http);
        httpConnector.setPort(port());
        httpConnector.setHost(config.host());
        httpConnector.setIdleTimeout(10000);
        return httpConnector;
    }

    private ThreadPool threadPool() {
        QueuedThreadPool _threadPool = new QueuedThreadPool();
        _threadPool.setMinThreads(config.minThreads());
        _threadPool.setMaxThreads(config.maxThreads());
        return _threadPool;
    }

    private int port() {
        int portNum = (config.port() <= 0) ? generatePort() : config.port();
        checkArgument(portNum > 0, "unable to generate port");
        return portNum;
    }

    public static int generatePort() {
        try (ServerSocket s = new ServerSocket(0)) {
            return s.getLocalPort();
        } catch (IOException e) {
            return -1;
        }
    }
}
