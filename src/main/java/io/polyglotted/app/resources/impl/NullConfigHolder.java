package io.polyglotted.app.resources.impl;

import io.polyglotted.app.resources.ConfigHolder;

public class NullConfigHolder implements ConfigHolder {
    @Override
    public <T> T properties(Class<T> configurationInterface) {
        return null;
    }

    @Override
    public <T> T properties(Class<T> configurationInterface, String source) {
        return null;
    }
}
