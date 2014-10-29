package org.polyglotted.webapp.http;

import fj.data.Option;
import org.polyglotted.webapp.resources.DefaultValue;
import org.polyglotted.webapp.resources.Properties;
import org.polyglotted.webapp.resources.Property;

@Properties(source = "jetty.properties")
public interface WebappConfig extends JettyConfig {
    public static final String Context_Path = "webapp.context.path";
    public static final String In_Ide = "webapp.in.ide";
    public static final String Log_Path = "webapp.log.path";
    public static final String Tmp_Directory = "jetty.webapp.tmp";

    @Property(name = Context_Path)
    @DefaultValue(value = "/")
    public String contextPath();

    @Property(name = In_Ide)
    @DefaultValue(value = "false")
    public boolean inIde();

    @Property(name = Log_Path)
    @DefaultValue(value = "target/accesslogs/yyyy_mm_dd.request.log")
    public String logPath();

    @Property(name = Tmp_Directory, optional = true)
    public Option<String> tempDirectory();
}
