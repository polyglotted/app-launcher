package org.polyglotted.webapp.http;

import org.polyglotted.webapp.resources.DefaultValue;
import org.polyglotted.webapp.resources.Properties;
import org.polyglotted.webapp.resources.Property;

@Properties(source = "jetty.properties")
public interface JettyConfig {
    public static final String JettyHttpBindInterface = "jetty.http.bindInterface";
    public static final String JettyHttpPort = "jetty.http.port";
    public static final String JettyHttpMinThreads = "jetty.http.minThreads";
    public static final String JettyHttpMaxThreads = "jetty.http.maxThreads";

    @Property(name = JettyHttpBindInterface)
    @DefaultValue(value = "0.0.0.0")
    public String host();

    @Property(name = JettyHttpPort)
    @DefaultValue(value = "8080")
    public int port();

    @Property(name = JettyHttpMinThreads)
    @DefaultValue(value = "10")
    public int minThreads();

    @Property(name = JettyHttpMaxThreads)
    @DefaultValue(value = "100")
    public int maxThreads();
}
