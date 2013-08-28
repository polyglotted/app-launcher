package org.polyglotted.webapp.launcher;

public enum LaunchStrategy {
    
    WebApp {
        public Server create() {
            return new WebServer();
        }
    },
    SpringApp {
        public Server create() {
            return new SpringServer();
        }
    };

    public abstract Server create();
}
