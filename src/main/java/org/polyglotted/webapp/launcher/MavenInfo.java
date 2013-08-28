package org.polyglotted.webapp.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import lombok.Data;

@Data
public class MavenInfo {
    public final String groupId;
    public final String artifactId;
    public final String version;
    public final String buildDate;

    public static MavenInfo parsePomProperties(InputStream in) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
        String version = "unknown";
        String buildDate = "unknown";
        String groupId = "unknown";
        String artifactId = "unknown";
        for (String line; (line = br.readLine()) != null;) {
            // #.*:.*:.*
            if (line.startsWith("#") && line.indexOf(':') != line.lastIndexOf(':')) {
                buildDate = line.substring(1);
                continue;
            }
            if (line.startsWith("version=")) version = line.substring(8);
            if (line.startsWith("groupId=")) groupId = line.substring(8);
            if (line.startsWith("artifactId=")) artifactId = line.substring(11);
        }
        return new MavenInfo(artifactId, groupId, version, buildDate);
    }

}
