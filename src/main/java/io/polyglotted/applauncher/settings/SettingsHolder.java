package io.polyglotted.applauncher.settings;

import com.typesafe.config.Config;

import java.util.Properties;

public interface SettingsHolder {

    Config config();

    <T> T proxy(Class<T> configurationInterface);

    Properties asProperties(String prefix, boolean includePrefix);

    boolean hasValue(String name);

    String stringValue(String name);

    int intValue(String name);

    boolean booleanValue(String name);

    long longValue(String name);

    double doubleValue(String name);

}
