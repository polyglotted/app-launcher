package io.polyglotted.app.http;

import org.eclipse.jetty.webapp.WebAppContext;

public interface JettyFeature {
    void enable(WebAppContext webAppContext);
}
