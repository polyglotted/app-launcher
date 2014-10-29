package org.polyglotted.webapp.resources;

import com.typesafe.config.Config;
import fj.data.Option;
import org.junit.Test;
import org.polyglotted.webapp.core.Gaveti;
import org.polyglotted.webapp.resources.impl.DefaultResources;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultResourcesTest {

    @Test
    public void properties() {
        Properties properties = localLoader().properties("some.properties");
        assertThat(properties.getProperty("aProperty"), is("some"));
    }

    @Test
    public void properties_based_config() {
        Config config = localLoader().config("some.properties");
        assertThat(config.getString("aProperty"), is("some"));
    }

    @Test
    public void hocon_based_config() {
        Config config = localLoader().config("some.conf");
        assertThat(config.getString("aPackage.aProperty"), is("some"));
    }

    @Test
    public void load_string() {
        String content = localLoader().string("some.properties");
        assertThat(content, is("aProperty=some"));
    }

    @Test
    public void instance_specific_resource() {
        Config config = instanceLoader().config("my.properties");
        assertThat(config.getString("aProperty"), is("instanceValue"));
    }

    @Test
    public void instance_specific_resource_in_subdirectory() {
        Config config = instanceLoader().config("bla.properties");
        assertThat(config.getString("myProperty"), is("subdir"));
    }

    @Test
    public void instance_specific_resource_without_extension() {
        assertThat(instanceLoader().string("ble"), is("bleble"));
    }

    @Test
    public void environment_specific_resource() {
        Config config = localLoader().config("my.properties");
        assertThat(config.getString("aProperty"), is("environmentValue"));
    }

    @Test
    public void environment_specific_resource_without_extension() {
        assertThat(localLoader().string("ble"), is("ble"));
    }

    @Test(expected = IllegalStateException.class)
    public void non_existent_resource() {
        instanceLoader().config("bla");
    }

    @Test
    public void falls_back_to_environment_specific_when_there_is_no_instance_file() {
        Properties properties = instanceLoader().properties("hello.properties");
        assertThat(properties.getProperty("aProperty"), is("helloValue"));
    }

    @Test
    public void falls_back_to_global_resource_when_there_is_no_instance_or_environment_resource() {
        Properties properties = instanceLoader().properties("some.properties");
        assertThat(properties.getProperty("aProperty"), is("some"));
    }

    @Test
    public void gavei_substitutes() {
        Config config = instanceLoader().config("substitute.properties");
        assertThat(config.getString("propertyWithSubstitute"), is("propertyWithSubstitute.polyglotted.resources.0.0.1.local.anInstance"));
        assertThat(config.getString("noSubstituted"), is("noSubstitutedValue"));
    }

    @Test
    public void gavei_substitutes_properties() {
        Properties properties = instanceLoader().propertiesWithPlaceholders("substitute.properties");
        assertThat(properties.getProperty("propertyWithSubstitute"), is("propertyWithSubstitute.polyglotted.resources.0.0.1.local.anInstance"));
        assertThat(properties.getProperty("noSubstituted"), is("noSubstitutedValue"));
        assertThat(properties.getProperty("propertyWithLocalSubstitute"), is("noSubstitutedValue"));
        assertThat(properties.getProperty("propertyWithLocalIntSubstitute"), is("1"));
        assertThat(properties.getProperty("usernameProperty"), is(System.getProperty("user.name")));
    }

    @Test
    public void no_substitution() {
        Properties properties = instanceLoader().properties("substitute.properties");
        assertThat(properties.getProperty("propertyWithSubstitute"), is("propertyWithSubstitute.${groupId}.${artifactId}.${version}.${environment}.${instanceName}"));
        assertThat(properties.getProperty("noSubstituted"), is("noSubstitutedValue"));
        assertThat(properties.getProperty("propertyWithLocalSubstitute"), is("${noSubstituted}"));
        assertThat(properties.getProperty("propertyWithLocalIntSubstitute"), is("${someIntValue}"));
        assertThat(properties.getProperty("usernameProperty"), is("${user.name}"));
    }

    @Test(expected = Exception.class) //should fail as there is no instance.name set
    public void gavei_substitutes_withoutInstance_fail() {
        Config config = localLoader().config("substitute.properties");
        assertThat(config.getString("propertyWithSubstitute"), is("propertyWithSubstitute.polyglotted.resources.0.0.1.local.anInstance"));
    }

    @Test
    public void gavei_substitutes_withoutInstance() {
        Config config = localLoader().config("substitute.noInstance.properties");
        assertThat(config.getString("propertyWithSubstitute"), is("propertyWithSubstitute.polyglotted.resources.0.0.1.local"));
    }

    private DefaultResources instanceLoader() {
        return new DefaultResources(new Gaveti("polyglotted", "resources", "0.0.1", "local",
                Option.<String>none(), Option.some("anInstance")));
    }

    private DefaultResources localLoader() {
        return new DefaultResources(new Gaveti("polyglotted", "resources", "0.0.1", "local"));
    }
}
