package org.polyglotted.webapp.launcher;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServer implements org.polyglotted.webapp.launcher.Server {
    private static final String LOG_PATH = "target/accesslogs/yyyy_mm_dd.request.log";
    private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/webapp";
    private static final String WEB_XML = "webapp/WEB-INF/web.xml";

    public static interface WebContext {
        public File getWarPath();

        public String getContextPath();
    }

    private PropertyHandler handler = new PropertyHandler();
    private Server server;

    public void start() throws Exception {
        server = new Server();
        server.setThreadPool(createThreadPool());
        server.addConnector(createConnector());
        server.setHandler(createHandlers());
        server.setStopAtShutdown(true);
        server.start();
        new Thread(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                server.join();
            }
        }).start();
    }

    @SneakyThrows
    public void stop() {
        server.stop();
    }

    private ThreadPool createThreadPool() {
        QueuedThreadPool _threadPool = new QueuedThreadPool();
        _threadPool.setMinThreads(handler.intProperty("jetty.http.minThreads", "10"));
        _threadPool.setMaxThreads(handler.intProperty("jetty.http.maxThreads", "100"));
        return _threadPool;
    }

    private SelectChannelConnector createConnector() {
        SelectChannelConnector _connector = new SelectChannelConnector();
        _connector.setPort(handler.intProperty("jetty.http.port", "8080"));
        _connector.setHost(handler.getProperty("jetty.http.bindInterface"));
        return _connector;
    }

    private HandlerCollection createHandlers() {
        WebAppContext _ctx = new WebAppContext();
        _ctx.setContextPath(handler.getProperty("webapp.context.path", "/"));

        if (handler.isTrue("webapp.in.ide", false)) {
            _ctx.setWar(ensureLocalFileExists(PROJECT_RELATIVE_PATH_TO_WEBAPP));
        }
        else {
            _ctx.setWar(getShadedWarUrl());
        }

        List<Handler> _handlers = new ArrayList<Handler>();
        _handlers.add(_ctx);

        HandlerList _contexts = new HandlerList();
        _contexts.setHandlers(_handlers.toArray(new Handler[0]));

        RequestLogHandler _log = new RequestLogHandler();
        _log.setRequestLog(createRequestLog());

        HandlerCollection _result = new HandlerCollection();
        _result.setHandlers(new Handler[] { _contexts, _log });

        return _result;
    }

    private RequestLog createRequestLog() {
        NCSARequestLog _log = new NCSARequestLog();
        File _logPath = new File(handler.getProperty("webapp.log.path", LOG_PATH));
        _logPath.getParentFile().mkdirs();
        _log.setFilename(_logPath.getPath());
        _log.setRetainDays(90);
        _log.setExtended(false);
        _log.setAppend(true);
        _log.setLogTimeZone("GMT");
        _log.setLogLatency(true);
        return _log;
    }

    private String getShadedWarUrl() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(WEB_XML);
        String _urlStr = resource.toString();
        return _urlStr.substring(0, _urlStr.length() - 15);
    }

    static String ensureLocalFileExists(String filePath) {
        if (!new File(filePath).isDirectory())
            throw new IllegalStateException("Unable to find webapp directory in the project");
        return filePath;
    }
}
