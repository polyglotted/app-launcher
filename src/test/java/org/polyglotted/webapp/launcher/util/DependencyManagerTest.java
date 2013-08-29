package org.polyglotted.webapp.launcher.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.polyglotted.webapp.launcher.util.TestUtils.asStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.google.common.io.CharStreams;

public class DependencyManagerTest {

    @Test
    public void testFailFilter() {
        assertFalse(new ZipFileFilter().accept(new File("src/test/resources/files/deps.txt")));
    }
    
    @Test
    public void testGetJarDependencies() throws IOException {
        String result = new DependencyManager().getJarDependencies();
        Readable reader = new InputStreamReader(asStream("files/deps.txt"));
        assertEquals(CharStreams.toString(reader), result);
    }
}
