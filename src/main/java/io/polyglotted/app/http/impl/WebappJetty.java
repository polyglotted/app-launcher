package io.polyglotted.app.http.impl;

import fj.F;
import fj.data.Option;
import io.polyglotted.app.http.Jetty;
import io.polyglotted.app.http.JettyFeature;
import io.polyglotted.app.http.AppConfig;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class WebappJetty implements Jetty {
    private static final String Web_Xml = "webapp/WEB-INF/web.xml";
    private static final String Project_Relative_Path_To_Webapp = "src/main/webapp";

    private final WebAppContext webAppContext;
    private final AppConfig config;
    private final List<JettyFeature> features;

    public WebappJetty(AppConfig config) {
        this(config, Collections.<JettyFeature>emptyList());
    }

    public WebappJetty(AppConfig config, List<JettyFeature> features) {
        this.config = config;
        this.features = features;
        this.webAppContext = webAppContext();
    }

    @Override
    public Server createServer() {
        final List<Handler> handlers = Arrays.<Handler>asList(webAppContext, requestLogHandler());
        Server server = new PlainJetty(config, handlers).createServer();
        for (JettyFeature feature : features) {
            addListener(server, feature);
        }

        return server;
    }

    private void addListener(final Server server, final JettyFeature feature) {
        server.addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {
            @Override
            public void lifeCycleStarted(LifeCycle event) {
                feature.enable(webAppContext);
            }
        });
    }

    private RequestLogHandler requestLogHandler() {
        RequestLogHandler log = new RequestLogHandler();
        log.setRequestLog(createRequestLog());
        return log;
    }

    private WebAppContext webAppContext() {
        WebAppContext context = new WebAppContext();
        context.setContextPath(config.contextPath());

        final Option<File> tempDirectory = tempDirectory();
        if (tempDirectory.isSome()) {
            context.setTempDirectory(tempDirectory.some());
        }

        if (config.inIde()) {
            checkState(new File(Project_Relative_Path_To_Webapp).isDirectory(), "Unable to find webapp directory");
            context.setWar(Project_Relative_Path_To_Webapp);
        } else {
            context.setWar(shadedWarUrl());
        }
        return context;
    }

    private String shadedWarUrl() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(Web_Xml);
        checkNotNull(resource, "No web.xml found at path: " + Web_Xml);
        String urlString = resource.toString();
        return urlString.substring(0, urlString.length() - 15);
    }

    private RequestLog createRequestLog() {
        NCSARequestLog _log = new NCSARequestLog();
        File _logPath = new File(config.logPath());
        _logPath.getParentFile().mkdirs();
        _log.setFilename(_logPath.getPath());
        _log.setRetainDays(90);
        _log.setExtended(false);
        _log.setAppend(true);
        _log.setLogTimeZone("GMT");
        _log.setLogLatency(true);
        return _log;
    }

    private Option<File> tempDirectory() {
        return config.tempDirectory().map(toFile()).filter(existsOrMkdir());
    }

    private static F<File, Boolean> existsOrMkdir() {
        return new F<File, Boolean>() {
            @Override
            public Boolean f(File file) {
                return file.exists() || file.mkdir();
            }
        };
    }

    private static F<String, File> toFile() {
        return new F<String, File>() {
            @Override
            public File f(String path) {
                return new File(path);
            }
        };
    }
}
