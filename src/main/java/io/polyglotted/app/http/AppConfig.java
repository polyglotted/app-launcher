package io.polyglotted.app.http;

import fj.data.Option;
import io.polyglotted.app.resources.Property;
import io.polyglotted.app.resources.DefaultValue;
import io.polyglotted.app.resources.Properties;

@Properties(source = "jetty.properties")
public interface AppConfig extends JettyConfig {
    String Context_Path = "app.context.path";
    String In_Ide = "app.in.ide";
    String Log_Path = "app.log.path";
    String Tmp_Directory = "jetty.app.tmp";

    @Property(name = Context_Path)
    @DefaultValue(value = "/")
    String contextPath();

    @Property(name = In_Ide)
    @DefaultValue(value = "false")
    boolean inIde();

    @Property(name = Log_Path)
    @DefaultValue(value = "target/accesslogs/yyyy_mm_dd.request.log")
    String logPath();

    @Property(name = Tmp_Directory, optional = true)
    Option<String> tempDirectory();
}
