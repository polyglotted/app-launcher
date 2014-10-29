package org.polyglotted.webapp.resources;

import com.typesafe.config.Config;

import java.io.InputStream;
import java.util.Properties;

public interface Resources {
    public byte[] bytes(String name);
    public String string(String name);
    public Properties properties(String name);
    public Properties propertiesWithPlaceholders(String name);
    public InputStream inputStream(String name);
    public Config config(String name);
}
