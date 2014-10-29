package org.polyglotted.webapp.resources;

public interface PropertiesFactory {

    <T> T properties(Class<T> configurationInterface);

    <T> T properties(Class<T> configurationInterface, String source);
}
