package org.polyglotted.webapp.launcher.util;

import static org.polyglotted.webapp.launcher.util.MavenInfo.parsePomProperties;

import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.SneakyThrows;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Returns detailed maven information on dependency jars")
public class DependencyManager {

    @ManagedOperation(description = "Returns maven jars and versions as a string")
    public String getJarDependencies() {
        return buildVersionInfo();
    }

    protected String buildVersionInfo() {
        StringBuilder sb = new StringBuilder();
        for (MavenInfo mi : getVersionInfo().values()) {
            String name = mi.artifactId.equals(mi.groupId) ? mi.artifactId : (mi.groupId + '/' + mi.artifactId);
            sb.append(name + " | " + mi.version + " | " + mi.buildDate + "\n");
        }
        return sb.toString();
    }

    @SneakyThrows
    protected Map<String, MavenInfo> getVersionInfo() {
        Map<String, MavenInfo> result = new TreeMap<String, MavenInfo>();
        ZipFileFilter filter = new ZipFileFilter();
        String[] parts = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String part : parts) {
            File file = new File(part);
            if (!filter.accept(file))
                continue;
            ZipFile zipFile = new JarFile(file);
            try {
                for (final Enumeration<? extends ZipEntry> enumeration = zipFile.entries(); enumeration
                        .hasMoreElements();) {
                    ZipEntry zipEntry = enumeration.nextElement();
                    final String name = zipEntry.getName();
                    if (!name.endsWith("pom.properties"))
                        continue;
                    result.put(name, parsePomProperties(zipFile.getInputStream(zipEntry)));
                }
            }
            finally {
                zipFile.close();
            }
        }
        return result;
    }
}
