package org.polyglotted.webapp.launcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.polyglotted.webapp.launcher.PropertyHandler.loadProperties;

import java.io.File;
import java.util.Properties;

import org.junit.Test;

public class PropertyHandlerTest {

    @Test
    public void testLoadProperties() {
        checkAsSystemProps(loadProperties("src/test/resources"));
    }

    @Test
    public void testLoadPropertiesFromKnownPath() {
        String fileName = "src/test/resources/files/unreadable.txt";
        File unreadFile = new File(fileName);
        unreadFile.setReadable(false);
        try {
            checkAsSystemProps(loadProperties(fileName));
        }
        finally {
            unreadFile.setReadable(true);
        }
    }

    private void checkAsSystemProps(Properties props) {
        assertNotNull(props);
        assertEquals(System.getProperties(), props);
    }
}
