package io.polyglotted.app.resources.impl;

import io.polyglotted.app.resources.ConfigFactory;

public class NullConfigFactory implements ConfigFactory {
    @Override
    public <T> T properties(Class<T> configurationInterface) {
        return null;
    }

    @Override
    public <T> T properties(Class<T> configurationInterface, String source) {
        return null;
    }
}
