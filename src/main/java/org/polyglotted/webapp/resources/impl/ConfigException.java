package org.polyglotted.webapp.resources.impl;

@SuppressWarnings("serial")
public class ConfigException extends RuntimeException {
    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
