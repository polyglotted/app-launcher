package io.polyglotted.app.http;

import io.polyglotted.app.resources.DefaultValue;
import io.polyglotted.app.resources.Properties;
import io.polyglotted.app.resources.Property;

@Properties(source = "jetty.properties")
public interface JettyConfig {
    String JettyHttpBindInterface = "jetty.http.bindInterface";
    String JettyHttpPort = "jetty.http.port";
    String JettyHttpMinThreads = "jetty.http.minThreads";
    String JettyHttpMaxThreads = "jetty.http.maxThreads";

    @Property(name = JettyHttpBindInterface)
    @DefaultValue(value = "0.0.0.0")
    String host();

    @Property(name = JettyHttpPort)
    @DefaultValue(value = "8080")
    int port();

    @Property(name = JettyHttpMinThreads)
    @DefaultValue(value = "10")
    int minThreads();

    @Property(name = JettyHttpMaxThreads)
    @DefaultValue(value = "100")
    int maxThreads();
}
