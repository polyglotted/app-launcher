package org.polyglotted.webapp.launcher.util;

import java.io.InputStream;

public class TestUtils {

    public static InputStream asStream(String path) {
        return TestUtils.class.getClassLoader().getResourceAsStream(path);
    }
}
