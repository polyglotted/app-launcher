package io.polyglotted.app.core;

import fj.F;
import fj.data.Option;
import fj.data.Stream;
import lombok.RequiredArgsConstructor;

import static fj.data.Stream.nil;
import static fj.data.Stream.single;

public class Paths {
    private final String environment;
    private final Option<String> instance;

    public Paths(String environment, Option<String> instance) {
        this.environment = environment;
        this.instance = instance;
    }

    public Stream<String> forName(String name) {
        return instancePath(name)
                .append(single(environmentPath(name)))
                .append(single(name));
    }

    private Stream<String> instancePath(String name) {
        if (instance.isSome()) {
            FileName fileName = FileName.from(name);
            return Stream.stream(slashInstancePath(fileName), dashInstancePath(fileName));
        } else {
            return nil();
        }
    }

    private String dashInstancePath(FileName fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(environment);
        builder.append("/");
        builder.append(fileName.name);
        builder.append("-");
        builder.append(instance.some());
        builder.append(fileName.extensionString());
        return builder.toString();
    }

    private String slashInstancePath(FileName fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(environment);
        builder.append("/");
        builder.append(instance.some());
        builder.append("/");
        builder.append(fileName.name);
        builder.append(fileName.extensionString());
        return builder.toString();
    }

    private String environmentPath(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append(environment);
        builder.append("/");
        builder.append(name);
        return builder.toString();
    }

    @RequiredArgsConstructor
    private static class FileName {
        public final String name;
        public final Option<String> extension;

        private String extensionString() {
            return extension.map(prependWithDot()).orSome("");
        }

        private F<String, String> prependWithDot() {
            return new F<String, String>() {
                @Override public String f(String string) {
                    return "." + string;
                }
            };
        }

        private static FileName from(String fullName) {
            int indexOfLastDot = fullName.lastIndexOf('.');
            if (indexOfLastDot == -1) {
                return new FileName(fullName, Option.<String>none());
            } else {
                String name = fullName.substring(0, indexOfLastDot);
                String extension = fullName.substring(indexOfLastDot + 1);
                return new FileName(name, Option.some(extension));
            }
        }
    }
}
