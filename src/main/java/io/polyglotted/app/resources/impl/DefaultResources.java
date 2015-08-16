package io.polyglotted.app.resources.impl;

import com.google.common.io.ByteStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import io.polyglotted.app.core.Gaveti;
import io.polyglotted.app.core.Paths;
import io.polyglotted.app.resources.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;
import static com.typesafe.config.ConfigSyntax.CONF;

public class DefaultResources implements Resources {

    private final Paths paths;
    private final Config gavetiConfig;
    private final Config systemConfig;

    public DefaultResources(Gaveti gaveti) {
        paths = new Paths(gaveti.environment(), gaveti.instanceName());
        gavetiConfig = gaveti.asConfig();
        systemConfig = ConfigFactory.systemProperties();
    }

    @Override
    public InputStream inputStream(String name) {
        final Stream<String> paths = this.paths.forName(name);
        return paths.filter(DefaultResources::resourceExists).map(DefaultResources::resourceInputStream).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Resource named \"" + name + "\" not found. "));
    }

    @Override
    public byte[] bytes(String name) {
        try {
            return ByteStreams.toByteArray(inputStream(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String string(String name) {
        return new String(bytes(name));
    }

    @Override
    public Properties properties(String name) {
        Properties properties = new Properties();
        InputStream inputStream = inputStream(name);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    @Override
    public Properties propertiesWithPlaceholders(String name) {
        Properties properties = properties(name);
        Config resolvedConfig = config(name);
        Properties resolved = new Properties();
        for (String property : properties.stringPropertyNames()) {
            resolved.setProperty(property, resolvedConfig.getString(property));
        }
        return resolved;
    }

    @Override
    public Config config(String name) {
        InputStreamReader reader = new InputStreamReader(inputStream(name));
        ConfigParseOptions configParseOptions = ConfigParseOptions.defaults().setSyntax(CONF);
        Config config = ConfigFactory.parseReader(reader, configParseOptions);
        return systemConfig.withFallback(gavetiConfig).withFallback(config).resolve();
    }

    @SuppressWarnings("serial")
    public static class ResourceNotFoundException extends IllegalStateException {
        public ResourceNotFoundException(String s) {
            super(s);
        }
    }

    private static boolean resourceExists(String name) {
        return resourceStreamOption(name).isPresent();
    }

    private static InputStream resourceInputStream(String name) {
        Optional<InputStream> result = resourceStreamOption(name);
        checkState(result.isPresent(), "No resource found for name: " + name);
        return result.get();
    }

    private static Optional<InputStream> resourceStreamOption(String name) {
        return Optional.ofNullable(DefaultResources.class.getResourceAsStream("/" + name));
    }
}

