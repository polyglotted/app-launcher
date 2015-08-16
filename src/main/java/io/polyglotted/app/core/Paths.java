package io.polyglotted.app.core;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

public class Paths {
    private final String environment;
    private final Optional<String> instance;

    public Paths(String environment, Optional<String> instance) {
        this.environment = environment;
        this.instance = instance;
    }

    public Stream<String> forName(String name) {
        return Stream.concat(Stream.concat(instancePath(name), environmentPath(name)), Stream.of(name));
    }

    private Stream<String> instancePath(String name) {
        if (instance.isPresent()) {
            FileName fileName = FileName.from(name);
            return Stream.of(slashInstancePath(fileName), dashInstancePath(fileName));
        } else {
            return Stream.empty();
        }
    }

    private String dashInstancePath(FileName fileName) {
        return environment + "/" + fileName.name + "-" + instance.get() + fileName.extensionString();
    }

    private String slashInstancePath(FileName fileName) {
        return environment + "/" + instance.get() + "/" + fileName.name + fileName.extensionString();
    }

    private Stream<String> environmentPath(String name) {
        return Stream.of(environment + "/" + name);
    }

    @RequiredArgsConstructor
    private static class FileName {
        public final String name;
        public final Optional<String> extension;

        private String extensionString() {
            return extension.map(s -> "." + s).orElse("");
        }

        private static FileName from(String fullName) {
            int indexOfLastDot = fullName.lastIndexOf('.');
            if (indexOfLastDot == -1) {
                return new FileName(fullName, Optional.<String>empty());
            } else {
                String name = fullName.substring(0, indexOfLastDot);
                String extension = fullName.substring(indexOfLastDot + 1);
                return new FileName(name, Optional.of(extension));
            }
        }
    }
}
