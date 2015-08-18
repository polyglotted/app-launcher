package io.polyglotted.applauncher.settings;

@SuppressWarnings("serial")
public class SettingsException extends RuntimeException {
    public SettingsException(String message) {
        super(message);
    }

    public SettingsException(Throwable cause) {
        super(cause);
    }
}
