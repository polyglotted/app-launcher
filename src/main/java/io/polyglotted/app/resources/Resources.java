package io.polyglotted.app.resources;

import com.typesafe.config.Config;

import java.io.InputStream;
import java.util.Properties;

public interface Resources {
    byte[] bytes(String name);
    String string(String name);
    Properties properties(String name);
    Properties propertiesWithPlaceholders(String name);
    InputStream inputStream(String name);
    Config config(String name);
}
