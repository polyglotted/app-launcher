package org.polyglotted.webapp.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import lombok.SneakyThrows;

public class PropertyHandler {

    private static final String LOCAL_PROPERTIES_FILE = "src/main/config/etc/sysargs.properties";
    private final Properties props = new Properties();

    public PropertyHandler() {
        loadPropsFromLocalFile();
        loadPropsFromGitRepo();
        overrideWithSystemProperties();
        props.list(System.out);
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
    private void loadPropsFromLocalFile() {
        File localFile = new File(LOCAL_PROPERTIES_FILE);
        InputStream inputStream = null;
        try {
            if (!localFile.exists() || !localFile.canRead()) {
                return;
            }
            inputStream = new FileInputStream(localFile);
            props.load(inputStream);
        }
        finally {
            if (inputStream != null)
                inputStream.close();
        }
    }

    private void overrideWithSystemProperties() {
        props.putAll(System.getProperties());
    }

    private void loadPropsFromGitRepo() {
        if("false".equalsIgnoreCase(System.getProperty("webapp.loadfrom.gitrepo", "false"))) {
            return;
        }
        if(safeClassLoadFailed("org.polyglotted.attributerepo.spring.AttribRepoResourceFactory")) {
            return;
        }
        props.putAll(loadRepoProperties());
    }

    private boolean safeClassLoadFailed(String className) {
        try {
            Class.forName(className);
            return false;
        }
        catch (ClassNotFoundException e) {
            return true;
        }
    }

    @SneakyThrows
    private static final Properties loadRepoProperties() {
        Class<?> clazz = Class.forName("org.polyglotted.attributerepo.spring.AttribRepoResourceFactory");
        Object factory = clazz.newInstance();

        Method setMethod = clazz.getMethod("setPropertiesFileLocation",
                Class.forName("org.springframework.core.io.Resource"));
        Object[] params = { null };
        setMethod.invoke(factory, params);

        Method loadMethod = clazz.getMethod("loadProperties", String.class);
        Properties result = (Properties) loadMethod.invoke(factory, "launcher.properties");

        return result;
    }
}
