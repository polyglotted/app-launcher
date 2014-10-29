package org.polyglotted.webapp.resources.impl;

import fj.F;
import fj.data.Option;

import java.io.InputStream;

import static com.google.common.base.Preconditions.checkState;

abstract class Classpath {
    static F<String, Boolean> resourceExists() {
        return new F<String, Boolean>() {
            @Override public Boolean f(String name) {
                return resourceExists(name);
            }
        };
    }

    private static boolean resourceExists(String name) {
        return resourceStreamOption(name).isSome();
    }

    private static InputStream resourceInputStream(String name) {
        Option<InputStream> result = resourceStreamOption(name);
        checkState(result.isSome(), "No resource found for name: " + name);
        return result.some();
    }

    static F<String, InputStream> toResourceInputStream() {
        return new F<String, InputStream>() {
            @Override public InputStream f(String name) {
                return resourceInputStream(name);
            }
        };
    }

    private static Option<InputStream> resourceStreamOption(String name) {
        return Option.fromNull(DefaultResources.class.getResourceAsStream("/" + name));
    }
}
