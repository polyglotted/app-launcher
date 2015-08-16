package io.polyglotted.app.resources;

public interface ConfigFactory {

    <T> T properties(Class<T> configurationInterface);

    <T> T properties(Class<T> configurationInterface, String source);
}
