package io.polyglotted.app.resources.impl;

import com.google.common.collect.ImmutableMap;
import io.polyglotted.app.resources.Config;
import io.polyglotted.app.resources.Property;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultConfigHolderTest {

    private static final String DEFAULT_STRING_PROPERTY = "stringProperty";
    private static final String DEFAULT_STRING_PROPERTY_VALUE = "string";
    private static final String DEFINED_STRING_PROPERTY = "definedStringProperty";
    private static final String DEFINED_STRING_PROPERTY_VALUE = "definedStringPropertyValue";
    private static final String DEFINED_WITH_DEFAULT_STRING_PROPERTY = "definedWithDefaultStringProperty";
    private static final String DEFINED_WITH_DEFAULT_STRING_PROPERTY_VALUE = "definedWithDefaultStringPropertyValue";
    private static final String OPTIONAL_STRING_PROPERTY = "optionalStringProperty";
    private static final String OPTIONAL_WITH_VALUE_STRING_PROPERTY = "optionalWithValueStringProperty";
    private static final String OPTIONAL_WITH_VALUE_STRING_PROPERTY_VALUE = "optionalWithValueStringPropertyValue";

    private static final String DEFAULT_INT_PROPERTY = "intProperty";
    private static final int DEFAULT_INT_PROPERTY_VALUE = 1;
    private static final String DEFINED_INT_PROPERTY = "definedIntProperty";
    private static final int DEFINED_INT_PROPERTY_VALUE = 12;
    private static final String DEFINED_WITH_DEFAULT_INT_PROPERTY = "definedWithDefaultIntProperty";
    private static final int DEFINED_WITH_DEFAULT_INT_PROPERTY_VALUE = 13;
    private static final String OPTIONAL_INT_PROPERTY = "optionalIntProperty";
    private static final String OPTIONAL_WITH_VALUE_INT_PROPERTY = "optionalWithValueIntProperty";
    private static final int OPTIONAL_WITH_VALUE_INT_PROPERTY_VALUE = 14;

    private static final String DEFAULT_LONG_PROPERTY = "longProperty";
    private static final long DEFAULT_LONG_PROPERTY_VALUE = 4L;
    private static final String DEFINED_LONG_PROPERTY = "definedLongProperty";
    private static final long DEFINED_LONG_PROPERTY_VALUE = 42L;
    private static final String DEFINED_WITH_DEFAULT_LONG_PROPERTY = "definedWithDefaultLongProperty";
    private static final long DEFINED_WITH_DEFAULT_LONG_PROPERTY_VALUE = 43L;
    private static final String OPTIONAL_LONG_PROPERTY = "optionalLongProperty";
    private static final String OPTIONAL_WITH_VALUE_LONG_PROPERTY = "optionalWithValueLongProperty";
    private static final long OPTIONAL_WITH_VALUE_LONG_PROPERTY_VALUE = 44L;

    private static final String DEFAULT_BOOL_PROPERTY = "boolProperty";
    private static final boolean DEFAULT_BOOL_PROPERTY_VALUE = false;
    private static final String DEFINED_BOOL_PROPERTY = "definedBoolProperty";
    private static final boolean DEFINED_BOOL_PROPERTY_VALUE = true;
    private static final String DEFINED_WITH_DEFAULT_BOOL_PROPERTY = "definedWithDefaultBoolProperty";
    private static final boolean DEFINED_WITH_DEFAULT_BOOL_PROPERTY_VALUE = false;
    private static final String OPTIONAL_BOOL_PROPERTY = "optionalBoolProperty";
    private static final String OPTIONAL_WITH_VALUE_BOOL_PROPERTY = "optionalWithValueBoolProperty";
    private static final boolean OPTIONAL_WITH_VALUE_BOOL_PROPERTY_VALUE = true;

    private static final String DEFAULT_DOUBLE_PROPERTY = "doubleProperty";
    private static final double DEFAULT_DOUBLE_PROPERTY_VALUE = 1.2;
    private static final String DEFINED_DOUBLE_PROPERTY = "definedDoubleProperty";
    private static final double DEFINED_DOUBLE_PROPERTY_VALUE = 1.2;
    private static final String DEFINED_WITH_DEFAULT_DOUBLE_PROPERTY = "definedWithDefaultDoubleProperty";
    private static final double DEFINED_WITH_DEFAULT_DOUBLE_PROPERTY_VALUE = 2.4;
    private static final String OPTIONAL_DOUBLE_PROPERTY = "optionalDoubleProperty";
    private static final String OPTIONAL_WITH_VALUE_DOUBLE_PROPERTY = "optionalWithValueDoubleProperty";
    private static final double OPTIONAL_WITH_VALUE_DOUBLE_PROPERTY_VALUE = 3.6;

    private MapValuesProvider mapConfigurationSourceProvider;

    private TestData<String> stringTestData;
    private TestData<Integer> intTestData;
    private TestData<Long> longTestData;
    private TestData<Boolean> boolTestData;
    private TestData<Double> doubleTestData;

    private static class TestData<T> {
        String defaultProperty;
        Object defaultPropertyValue;
        String definedProperty;
        T definedPropertyValue;
        String optionalProperty;
        String optionalWithValueProperty;
        T optionalWithValuePropertyValue;

        String definedWithDefaultProperty;
        T definedWithDefaultPropertyValue;
    }

    @BeforeTest
    public void setUp() throws Exception {

        stringTestData = new TestData<>();
        stringTestData.defaultProperty = DEFAULT_STRING_PROPERTY;
        stringTestData.defaultPropertyValue = DEFAULT_STRING_PROPERTY_VALUE;
        stringTestData.definedProperty = DEFINED_STRING_PROPERTY;
        stringTestData.definedPropertyValue = DEFINED_STRING_PROPERTY_VALUE;
        stringTestData.definedWithDefaultProperty = DEFINED_WITH_DEFAULT_STRING_PROPERTY;
        stringTestData.definedWithDefaultPropertyValue = DEFINED_WITH_DEFAULT_STRING_PROPERTY_VALUE;
        stringTestData.optionalProperty = OPTIONAL_STRING_PROPERTY;
        stringTestData.optionalWithValueProperty = OPTIONAL_WITH_VALUE_STRING_PROPERTY;
        stringTestData.optionalWithValuePropertyValue = OPTIONAL_WITH_VALUE_STRING_PROPERTY_VALUE;

        intTestData = new TestData<>();
        intTestData.defaultProperty = DEFAULT_INT_PROPERTY;
        intTestData.defaultPropertyValue = DEFAULT_INT_PROPERTY_VALUE;
        intTestData.definedProperty = DEFINED_INT_PROPERTY;
        intTestData.definedPropertyValue = DEFINED_INT_PROPERTY_VALUE;
        intTestData.definedWithDefaultProperty = DEFINED_WITH_DEFAULT_INT_PROPERTY;
        intTestData.definedWithDefaultPropertyValue = DEFINED_WITH_DEFAULT_INT_PROPERTY_VALUE;
        intTestData.optionalProperty = OPTIONAL_INT_PROPERTY;
        intTestData.optionalWithValueProperty = OPTIONAL_WITH_VALUE_INT_PROPERTY;
        intTestData.optionalWithValuePropertyValue = OPTIONAL_WITH_VALUE_INT_PROPERTY_VALUE;

        longTestData = new TestData<>();
        longTestData.defaultProperty = DEFAULT_LONG_PROPERTY;
        longTestData.defaultPropertyValue = DEFAULT_LONG_PROPERTY_VALUE;
        longTestData.definedProperty = DEFINED_LONG_PROPERTY;
        longTestData.definedPropertyValue = DEFINED_LONG_PROPERTY_VALUE;
        longTestData.definedWithDefaultProperty = DEFINED_WITH_DEFAULT_LONG_PROPERTY;
        longTestData.definedWithDefaultPropertyValue = DEFINED_WITH_DEFAULT_LONG_PROPERTY_VALUE;
        longTestData.optionalProperty = OPTIONAL_LONG_PROPERTY;
        longTestData.optionalWithValueProperty = OPTIONAL_WITH_VALUE_LONG_PROPERTY;
        longTestData.optionalWithValuePropertyValue = OPTIONAL_WITH_VALUE_LONG_PROPERTY_VALUE;

        boolTestData = new TestData<>();
        boolTestData.defaultProperty = DEFAULT_BOOL_PROPERTY;
        boolTestData.defaultPropertyValue = DEFAULT_BOOL_PROPERTY_VALUE;
        boolTestData.definedProperty = DEFINED_BOOL_PROPERTY;
        boolTestData.definedPropertyValue = DEFINED_BOOL_PROPERTY_VALUE;
        boolTestData.definedWithDefaultProperty = DEFINED_WITH_DEFAULT_BOOL_PROPERTY;
        boolTestData.definedWithDefaultPropertyValue = DEFINED_WITH_DEFAULT_BOOL_PROPERTY_VALUE;
        boolTestData.optionalProperty = OPTIONAL_BOOL_PROPERTY;
        boolTestData.optionalWithValueProperty = OPTIONAL_WITH_VALUE_BOOL_PROPERTY;
        boolTestData.optionalWithValuePropertyValue = OPTIONAL_WITH_VALUE_BOOL_PROPERTY_VALUE;

        doubleTestData = new TestData<>();
        doubleTestData.defaultProperty = DEFAULT_DOUBLE_PROPERTY;
        doubleTestData.defaultPropertyValue = DEFAULT_DOUBLE_PROPERTY_VALUE;
        doubleTestData.definedProperty = DEFINED_DOUBLE_PROPERTY;
        doubleTestData.definedPropertyValue = DEFINED_DOUBLE_PROPERTY_VALUE;
        doubleTestData.definedWithDefaultProperty = DEFINED_WITH_DEFAULT_DOUBLE_PROPERTY;
        doubleTestData.definedWithDefaultPropertyValue = DEFINED_WITH_DEFAULT_DOUBLE_PROPERTY_VALUE;
        doubleTestData.optionalProperty = OPTIONAL_DOUBLE_PROPERTY;
        doubleTestData.optionalWithValueProperty = OPTIONAL_WITH_VALUE_DOUBLE_PROPERTY;
        doubleTestData.optionalWithValuePropertyValue = OPTIONAL_WITH_VALUE_DOUBLE_PROPERTY_VALUE;

        Map<String, Object> propertiesMap = new HashMap<>();
        addProperties(propertiesMap, stringTestData);
        addProperties(propertiesMap, intTestData);
        addProperties(propertiesMap, longTestData);
        addProperties(propertiesMap, boolTestData);
        addProperties(propertiesMap, doubleTestData);

        mapConfigurationSourceProvider = new MapValuesProvider(ImmutableMap.of("source", propertiesMap));
    }

    private <T> void addProperties(Map<String, Object> map, TestData<T> testData) {
        map.put(testData.definedProperty, testData.definedPropertyValue);
        map.put(testData.definedWithDefaultProperty, testData.definedWithDefaultPropertyValue);
        map.put(testData.optionalWithValueProperty, testData.optionalWithValuePropertyValue);
    }

    @Test
    public void test_alternativeSource() {
        //Default location
        Map<String, Object> defaultSourceProperties = ImmutableMap.<String, Object>of(
                "property1", "value1",
                "property2", "value2"
        );
        //Alternative location
        Map<String, Object> alternativeSourceProperties = ImmutableMap.<String, Object>of(
                "property1", "source2.value1",
                "property2", "source2.value2"
        );
        DefaultConfigHolder propertiesFactory = new DefaultConfigHolder(new MapValuesProvider(
                ImmutableMap.of(
                        "source", defaultSourceProperties,
                        "source2", alternativeSourceProperties
                )
        ));

        AlternativeSourcesTestProperties defaultProperties =
                propertiesFactory.properties(AlternativeSourcesTestProperties.class);

        assertThat(defaultProperties.property1(), is("value1"));
        assertThat(defaultProperties.property2(), is("value2"));
    }

    @Config(source = "source")
    public interface AlternativeSourcesTestProperties {
        @Property(name = "property1")
        String property1();

        @Property(name = "property2")
        String property2();
    }

    @Test(expectedExceptions = ConfigException.class, expectedExceptionsMessageRegExp = ".* is not an interface")
    public void configurationIsNotAnInterface() {
        new DefaultConfigHolder(mapConfigurationSourceProvider).properties(TestConfigNotInterface.class);
    }

    @Test(expectedExceptions = ConfigException.class, expectedExceptionsMessageRegExp = ".* is not public")
    public void configurationIsNotPublic() {
        new DefaultConfigHolder(mapConfigurationSourceProvider).properties(TestConfigNotPublic.class);
    }

    @Test(expectedExceptions = ConfigException.class,
            expectedExceptionsMessageRegExp = ".* must be annotated with @Config annotation")
    public void configurationHasNoAnnotation() {
        new DefaultConfigHolder(mapConfigurationSourceProvider).properties(TestConfigNoAnnotation.class);
    }

    @Test(expectedExceptions = ConfigException.class,
            expectedExceptionsMessageRegExp = "No properties are defined in @Config .*")
    public void configurationsAreNotDefined() {
        new DefaultConfigHolder(mapConfigurationSourceProvider).properties(TestConfigNoProperties.class);
    }

    @Test(expectedExceptions = ConfigException.class,
            expectedExceptionsMessageRegExp = "Missing property .* expected in configuration .*")
    public void configurationsAreMissing() {
        new DefaultConfigHolder(mapConfigurationSourceProvider).properties(TestConfigMissingType.class);
    }

    @Test(expectedExceptions = ConfigException.class,
            expectedExceptionsMessageRegExp = "Method .* annotated with @Property has unsupported return type. .*")
    public void configurationsAreInvalid() {
        new DefaultConfigHolder(mapConfigurationSourceProvider).properties(TestConfigUnsupportedType.class);
    }

    @Test
    public void successfulConfiguration() {
        DefaultConfigHolder configurationFactory = new DefaultConfigHolder(mapConfigurationSourceProvider);
        TestConfigValid configuration = configurationFactory.properties(TestConfigValid.class);

        assertThat(configuration.defaultStringProperty(), is(equalTo(stringTestData.defaultPropertyValue)));
        assertThat(configuration.definedStringProperty(), is(equalTo(stringTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultStringProperty(), is(equalTo(stringTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalStringProperty(), is(equalTo(Optional.<String>empty())));
        Optional<String> of = Optional.of(stringTestData.optionalWithValuePropertyValue);
        Optional<String> actual = configuration.optionalWithValueStringProperty();
        assertThat(actual, is(equalTo(of)));

        assertThat(configuration.defaultIntProperty(), is(equalTo(intTestData.defaultPropertyValue)));
        assertThat(configuration.definedIntProperty(), is(equalTo(intTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultIntProperty(), is(equalTo(intTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalIntProperty(), is(equalTo(Optional.<Integer>empty())));
        assertThat(configuration.optionalWithValueIntProperty(), is(equalTo(Optional.of(intTestData.optionalWithValuePropertyValue))));

        assertThat(configuration.defaultLongProperty(), is(equalTo(longTestData.defaultPropertyValue)));
        assertThat(configuration.definedLongProperty(), is(equalTo(longTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultLongProperty(), is(equalTo(longTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalLongProperty(), is(equalTo(Optional.<Long>empty())));
        assertThat(configuration.optionalWithValueLongProperty(), is(equalTo(Optional.of(longTestData.optionalWithValuePropertyValue))));

        assertThat(configuration.defaultBoolProperty(), is(equalTo(boolTestData.defaultPropertyValue)));
        assertThat(configuration.definedBoolProperty(), is(equalTo(boolTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultBoolProperty(), is(equalTo(boolTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalBoolProperty(), is(equalTo(Optional.<Boolean>empty())));
        assertThat(configuration.optionalWithValueBoolProperty(), is(equalTo(Optional.of(boolTestData.optionalWithValuePropertyValue))));

        assertThat(configuration.defaultDoubleProperty(), is(equalTo(doubleTestData.defaultPropertyValue)));
        assertThat(configuration.definedDoubleProperty(), is(equalTo(doubleTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultDoubleProperty(), is(equalTo(doubleTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalDoubleProperty(), is(equalTo(Optional.<Double>empty())));
        assertThat(configuration.optionalWithValueDoubleProperty(), is(equalTo(Optional.of(doubleTestData.optionalWithValuePropertyValue))));
    }

    @Config(source = "source")
    public static class TestConfigNotInterface {
    }

    @Config(source = "source")
    interface TestConfigNotPublic {
    }

    public interface TestConfigNoAnnotation {
    }

    @Config(source = "source")
    public interface TestConfigNoProperties {
    }

    @Config(source = "source")
    @SuppressWarnings("unused")
    public interface TestConfigMissingType {

        @Property(name = "stringProperty")
        Double stringProperty();
    }

    @Config(source = "source")
    @SuppressWarnings("unused")
    public interface TestConfigUnsupportedType {

        @Property(name = "dateProperty")
        Date dateProperty();
    }

    @Config(source = "source")
    public interface TestConfigValid {

        @Property(name = DEFINED_STRING_PROPERTY)
        String definedStringProperty();

        @Property(name = DEFINED_WITH_DEFAULT_STRING_PROPERTY)
        default String definedWithDefaultStringProperty() {
            return "definedWithDefaultStringProperty default value";
        }

        @Property(name = DEFAULT_STRING_PROPERTY)
        default String defaultStringProperty() {
            return DEFAULT_STRING_PROPERTY_VALUE;
        }

        @Property(name = OPTIONAL_STRING_PROPERTY, optional = true)
        Optional<String> optionalStringProperty();

        @Property(name = OPTIONAL_WITH_VALUE_STRING_PROPERTY, optional = true)
        Optional<String> optionalWithValueStringProperty();

        @Property(name = DEFAULT_INT_PROPERTY)
        default int defaultIntProperty() {
            return DEFAULT_INT_PROPERTY_VALUE;
        }

        @Property(name = DEFINED_INT_PROPERTY)
        int definedIntProperty();

        @Property(name = DEFINED_WITH_DEFAULT_INT_PROPERTY)
        default int definedWithDefaultIntProperty() {
            return 131;
        }

        @Property(name = OPTIONAL_INT_PROPERTY, optional = true)
        Optional<Integer> optionalIntProperty();

        @Property(name = OPTIONAL_WITH_VALUE_INT_PROPERTY, optional = true)
        Optional<Integer> optionalWithValueIntProperty();

        @Property(name = DEFAULT_LONG_PROPERTY)
        default long defaultLongProperty() {
            return DEFAULT_LONG_PROPERTY_VALUE;
        }

        @Property(name = DEFINED_LONG_PROPERTY)
        long definedLongProperty();

        @Property(name = DEFINED_WITH_DEFAULT_LONG_PROPERTY)
        default long definedWithDefaultLongProperty() {
            return 431L;
        }

        @Property(name = OPTIONAL_LONG_PROPERTY, optional = true)
        Optional<Long> optionalLongProperty();

        @Property(name = OPTIONAL_WITH_VALUE_LONG_PROPERTY, optional = true)
        Optional<Long> optionalWithValueLongProperty();

        @Property(name = DEFAULT_BOOL_PROPERTY)
        default boolean defaultBoolProperty() {
            return DEFAULT_BOOL_PROPERTY_VALUE;
        }

        @Property(name = DEFINED_BOOL_PROPERTY)
        boolean definedBoolProperty();

        @Property(name = DEFINED_WITH_DEFAULT_BOOL_PROPERTY)
        default boolean definedWithDefaultBoolProperty() {
            return true;
        }

        @Property(name = OPTIONAL_BOOL_PROPERTY, optional = true)
        Optional<Boolean> optionalBoolProperty();

        @Property(name = OPTIONAL_WITH_VALUE_BOOL_PROPERTY, optional = true)
        Optional<Boolean> optionalWithValueBoolProperty();

        @Property(name = DEFAULT_DOUBLE_PROPERTY)
        default double defaultDoubleProperty() {
            return DEFAULT_DOUBLE_PROPERTY_VALUE;
        }

        @Property(name = DEFINED_DOUBLE_PROPERTY)
        double definedDoubleProperty();

        @Property(name = DEFINED_WITH_DEFAULT_DOUBLE_PROPERTY)
        default double definedWithDefaultDoubleProperty() {
            return 1.2;
        }

        @Property(name = OPTIONAL_DOUBLE_PROPERTY, optional = true)
        Optional<Double> optionalDoubleProperty();

        @Property(name = OPTIONAL_WITH_VALUE_DOUBLE_PROPERTY, optional = true)
        Optional<Double> optionalWithValueDoubleProperty();
    }
}
