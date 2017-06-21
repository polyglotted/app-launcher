package io.polyglotted.applauncher.settings;

import com.typesafe.config.Config;
import io.polyglotted.applauncher.crypto.CryptoClient;

import java.util.Map;

public interface SettingsHolder {

    Config config();

    default <T> T proxy(Class<T> configurationInterface) { return proxy(configurationInterface, new CryptoClient()); }

    <T> T proxy(Class<T> configurationInterface, CryptoClient cryptoClient);

    Map<String, Object> asProperties(String prefix, boolean includePrefix);

    boolean hasValue(String name);

    String stringValue(String name);

    int intValue(String name);

    boolean booleanValue(String name);

    long longValue(String name);

    double doubleValue(String name);

}
