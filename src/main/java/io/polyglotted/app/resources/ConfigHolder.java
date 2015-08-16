package io.polyglotted.app.resources;

public interface ConfigHolder {

    <T> T properties(Class<T> configurationInterface);

    <T> T properties(Class<T> configurationInterface, String source);
}
