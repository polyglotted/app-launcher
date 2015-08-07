package io.polyglotted.app.launcher;

import io.polyglotted.app.core.Gaveti;
import io.polyglotted.app.core.Overrides;

import javax.inject.Inject;

public class Starter {
    @Inject private Overrides overrides;
    @Inject public Gaveti gaveti = null;

    public static boolean started = false;
    public static String artifactId = "";
    public static String aProperty = "";

    public void start() {
        started = true;
        artifactId = gaveti.artifactId();
        aProperty = overrides.string("aProperty").orSome("");
    }

    public void stop() {
        reset();
    }

    public static void reset() {
        started = false;
    }
}
