package io.polyglotted.applauncher.server;

import io.polyglotted.applauncher.settings.Attribute;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MainTest {

    @BeforeMethod
    public void unsetSystem() {
        System.clearProperty(Main.SERVER_CLASS);
    }

    @Test(expectedExceptions = {ClassNotFoundException.class})
    public void shouldFailWithoutServerClassDef() {
        System.setProperty(Main.SERVER_CLASS, "foo");
        Main.main(new String[0]);
    }

    @Test(expectedExceptions = {InstantiationException.class})
    public void shouldFailWithoutAssignableServer() {
        System.setProperty(Main.SERVER_CLASS, Attribute.class.getCanonicalName());
        Main.main(new String[0]);
    }

    @Test
    public void shouldServe() {
        System.setProperty(Main.SERVER_CLASS, TestServer.class.getCanonicalName());
        Main.main(new String[0]);
    }

}
