package org.polyglotted.webapp.launcher.util;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.webapp.launcher.util.TestUtils.asStream;

import org.junit.Test;

public class MavenInfoTest {

    @Test
    public void testParsePomProperties() throws Exception {
        MavenInfo mavenInfo = MavenInfo.parsePomProperties(asStream("files/pom.properties"));
        assertEquals("org.polyglotted", mavenInfo.groupId);
        assertEquals("webapp-launcher", mavenInfo.artifactId);
        assertEquals("1.0.3-SNAPSHOT", mavenInfo.version);
        assertEquals("Sun Aug 25 20:05:59 BST 2013", mavenInfo.buildDate);
    }
}
