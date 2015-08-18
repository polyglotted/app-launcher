package io.polyglotted.applauncher.settings;

import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DefaultSettingsHolderTest {

    @Test
    public void shouldRespectReference() {
        SettingsHolder settings = new DefaultSettingsHolder();
        SampleSettings sampleSettings = settings.proxy(SampleSettings.class);
        assertThat(sampleSettings.a(), is(equalTo("reference_a")));
        assertThat(settings.stringValue("sample.a"), is(equalTo("reference_a")));
    }

    @Test
    public void shouldRespectApplication() {
        SettingsHolder settings = new DefaultSettingsHolder();
        SampleSettings sampleSettings = settings.proxy(SampleSettings.class);
        assertThat(sampleSettings.b(), is(equalTo("application_b")));
        assertThat(settings.stringValue("sample.b"), is(equalTo("application_b")));
    }

    @Test
    public void shouldRespectSystem() {
        System.setProperty("sample.c", "system_c");
        ConfigFactory.invalidateCaches();
        SettingsHolder settings = new DefaultSettingsHolder();
        SampleSettings sampleSettings = settings.proxy(SampleSettings.class);
        assertThat(sampleSettings.c(), is(equalTo("system_c")));
        assertThat(settings.stringValue("sample.c"), is(equalTo("system_c")));
    }

    @Test
    public void shouldDefault() {
        SettingsHolder settings = new DefaultSettingsHolder();
        SampleSettings sampleSettings = settings.proxy(SampleSettings.class);
        assertThat(sampleSettings.d(), is(equalTo("d")));
    }

    @Test
    public void shouldExposeBooleanProperty() {
        SettingsHolder settings = new DefaultSettingsHolder();
        assertThat(settings.booleanValue("sample.boolean"), is(equalTo(false)));
    }

    @Test
    public void shouldExposeLongProperty() {
        SettingsHolder settings = new DefaultSettingsHolder();
        assertThat(settings.longValue("sample.long"), is(equalTo(2147483648L)));
    }

    @Test
    public void shouldExposeIntProperty() {
        SettingsHolder settings = new DefaultSettingsHolder();
        assertThat(settings.intValue("sample.int"), is(equalTo(32)));
    }

    @Test
    public void shouldExposeDoubleProperty() {
        SettingsHolder settings = new DefaultSettingsHolder();
        assertThat(settings.doubleValue("sample.double"), is(equalTo(3.1415)));
    }

    @Test
    public void shouldAnswerHasValue() {
        SettingsHolder settings = new DefaultSettingsHolder();
        assertThat(settings.hasValue("sample.int"), is(equalTo(true)));
        assertThat(settings.hasValue("sample.integer"), is(equalTo(false)));
    }

    @Test
    public void shouldAsPropertiesWithoutPrefix() {
        SettingsHolder settings = new DefaultSettingsHolder();
        Properties props = settings.asProperties("elastic", false);
        assertThat(props.size(), is(equalTo(3)));
        assertThat(props.get("a"), is(equalTo("elastic_a")));
        assertThat(props.get("b"), is(equalTo("elastic_b")));
        assertThat(props.get("c"), is(equalTo("elastic_c")));
    }

    @Test
    public void shouldAsPropertiesWithPrefix() {
        SettingsHolder settings = new DefaultSettingsHolder();
        Properties props = settings.asProperties("elastic", true);
        assertThat(props.size(), is(equalTo(3)));
        assertThat(props.get("elastic.a"), is(equalTo("elastic_a")));
        assertThat(props.get("elastic.b"), is(equalTo("elastic_b")));
        assertThat(props.get("elastic.c"), is(equalTo("elastic_c")));
    }

    @Test(expectedExceptions = {ConfigException.class})
    public void shouldErrorForUnknownKey() {
        SettingsHolder settings = new DefaultSettingsHolder();
        settings.stringValue("sample.d");
    }

    @Settings
    public interface SampleSettings {
        @Attribute(name = "sample.a")
        String a();

        @Attribute(name = "sample.b")
        String b();

        @Attribute(name = "sample.c")
        default String c() { return "c"; }

        @Attribute(name = "sample.d")
        default String d() { return "d"; }
    }
}