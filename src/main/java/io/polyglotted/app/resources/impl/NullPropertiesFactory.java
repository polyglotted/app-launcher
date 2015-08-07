package io.polyglotted.app.resources.impl;

import io.polyglotted.app.resources.PropertiesFactory;

public class NullPropertiesFactory implements PropertiesFactory {
    @Override
    public <T> T properties(Class<T> configurationInterface) {
        return null;
    }

    @Override
    public <T> T properties(Class<T> configurationInterface, String source) {
        return null;
    }
}
