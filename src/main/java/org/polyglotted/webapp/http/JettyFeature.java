package org.polyglotted.webapp.http;

import org.eclipse.jetty.webapp.WebAppContext;

public interface JettyFeature {
    void enable(WebAppContext webAppContext);
}
