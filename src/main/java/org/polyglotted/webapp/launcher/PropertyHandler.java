package org.polyglotted.webapp.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import lombok.SneakyThrows;

public class PropertyHandler {

    private static final String LOCAL_PROPERTIES_FILE = "src/main/config/etc/sysargs.properties";
    private final Properties props;

    public PropertyHandler() {
        props = loadProperties(LOCAL_PROPERTIES_FILE);
    }

    public boolean isTrue(String prop, boolean def) {
        return "true".equalsIgnoreCase(props.getProperty(prop, String.valueOf(def)));
    }

    public String getProperty(String prop) {
        return props.getProperty(prop);
    }

    public String getProperty(String prop, String def) {
        return props.getProperty(prop, def);
    }

    public int intProperty(String prop, String def) {
        return Integer.parseInt(props.getProperty(prop, def));
    }

    @SneakyThrows
    static Properties loadProperties(String filePath) {
        Properties result = new Properties();
        InputStream inputStream = null;
        try {
            File localFile = new File(filePath);
            if (!localFile.isFile() || !localFile.canRead()) {
                result.putAll(System.getProperties());
                return result;
            }
            inputStream = new FileInputStream(localFile);
            result.load(inputStream);
            result.putAll(System.getProperties());
        }
        finally {
            if (inputStream != null)
                inputStream.close();
        }
        return result;
    }
}
