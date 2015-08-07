package org.polyglotted.webapp.resources.impl;

import com.google.common.collect.ImmutableMap;
import fj.data.Option;
import org.junit.Before;
import org.junit.Test;
import org.polyglotted.webapp.resources.DefaultValue;
import org.polyglotted.webapp.resources.Properties;
import org.polyglotted.webapp.resources.Property;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PropertiesFactoryTest {

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
    private static final String DEFAULT_INT_PROPERTY_VALUE = "1";
    private static final String DEFINED_INT_PROPERTY = "definedIntProperty";
    private static final int DEFINED_INT_PROPERTY_VALUE = 12;
    private static final String DEFINED_WITH_DEFAULT_INT_PROPERTY = "definedWithDefaultIntProperty";
    private static final int DEFINED_WITH_DEFAULT_INT_PROPERTY_VALUE = 13;
    private static final String OPTIONAL_INT_PROPERTY = "optionalIntProperty";
    private static final String OPTIONAL_WITH_VALUE_INT_PROPERTY = "optionalWithValueIntProperty";
    private static final int OPTIONAL_WITH_VALUE_INT_PROPERTY_VALUE = 14;

    private static final String DEFAULT_LONG_PROPERTY = "longProperty";
    private static final String DEFAULT_LONG_PROPERTY_VALUE = "4";
    private static final String DEFINED_LONG_PROPERTY = "definedLongProperty";
    private static final long DEFINED_LONG_PROPERTY_VALUE = 42L;
    private static final String DEFINED_WITH_DEFAULT_LONG_PROPERTY = "definedWithDefaultLongProperty";
    private static final long DEFINED_WITH_DEFAULT_LONG_PROPERTY_VALUE = 43L;
    private static final String OPTIONAL_LONG_PROPERTY = "optionalLongProperty";
    private static final String OPTIONAL_WITH_VALUE_LONG_PROPERTY = "optionalWithValueLongProperty";
    private static final long OPTIONAL_WITH_VALUE_LONG_PROPERTY_VALUE = 44L;

    private static final String DEFAULT_BOOL_PROPERTY = "boolProperty";
    private static final String DEFAULT_BOOL_PROPERTY_VALUE = "false";
    private static final String DEFINED_BOOL_PROPERTY = "definedBoolProperty";
    private static final boolean DEFINED_BOOL_PROPERTY_VALUE = true;
    private static final String DEFINED_WITH_DEFAULT_BOOL_PROPERTY = "definedWithDefaultBoolProperty";
    private static final boolean DEFINED_WITH_DEFAULT_BOOL_PROPERTY_VALUE = false;
    private static final String OPTIONAL_BOOL_PROPERTY = "optionalBoolProperty";
    private static final String OPTIONAL_WITH_VALUE_BOOL_PROPERTY = "optionalWithValueBoolProperty";
    private static final boolean OPTIONAL_WITH_VALUE_BOOL_PROPERTY_VALUE = true;

    private static final String DEFAULT_DOUBLE_PROPERTY = "doubleProperty";
    private static final String DEFAULT_DOUBLE_PROPERTY_VALUE = "1.2";
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
        String defaultPropertyValue;
        String definedProperty;
        T definedPropertyValue;
        String optionalProperty;
        String optionalWithValueProperty;
        T optionalWithValuePropertyValue;

        String definedWithDefaultProperty;
        T definedWithDefaultPropertyValue;
    }

    @Before
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
        DefaultPropertiesFactory propertiesFactory = new DefaultPropertiesFactory(new MapValuesProvider(
                ImmutableMap.of(
                        "source", defaultSourceProperties,
                        "source2", alternativeSourceProperties
                )
        ));

        AlternativeSourcesTestProperties defaultProperties =
                propertiesFactory.properties(AlternativeSourcesTestProperties.class);
        AlternativeSourcesTestProperties alternativePoperties =
                propertiesFactory.properties(AlternativeSourcesTestProperties.class, "source2");

        assertThat(defaultProperties.property1(), is("value1"));
        assertThat(defaultProperties.property2(), is("value2"));
    }

    @Properties(source = "source")
    private interface AlternativeSourcesTestProperties {
        @Property(name = "property1")
        String property1();

        @Property(name = "property2")
        String property2();
    }

    @Test(expected = ConfigException.class)
    public void test_getConfiguraiton_noAnnotation_map() {
        test_getConfiguraiton_noAnnotation(new DefaultPropertiesFactory(mapConfigurationSourceProvider));
    }

    private TestConfigNoAnnotation test_getConfiguraiton_noAnnotation(DefaultPropertiesFactory configurationFactory) {
        return configurationFactory.properties(TestConfigNoAnnotation.class);
    }

    @Test(expected = ConfigException.class)
    public void test_getConfiguraiton_noProperties_map() {
        test_getConfiguraiton_noProperties(new DefaultPropertiesFactory(mapConfigurationSourceProvider));
    }

    private void test_getConfiguraiton_noProperties(DefaultPropertiesFactory configurationFactory) {
        configurationFactory.properties(TestConfigNoProperties.class);
    }

    @Test(expected = ConfigException.class)
    public void test_getConfiguraiton_unsupportedType_map() {
        test_getConfiguraiton_unsupportedType(new DefaultPropertiesFactory(mapConfigurationSourceProvider));
    }

    private void test_getConfiguraiton_unsupportedType(DefaultPropertiesFactory configurationFactory) {
        configurationFactory.properties(TestConfigUnsupportedType.class);
    }

    @Test
    public void test_success_map() {
        DefaultPropertiesFactory configurationFactory = new DefaultPropertiesFactory(mapConfigurationSourceProvider);
        TestConfigValid configuration = configurationFactory.properties(TestConfigValid.class);

        assertThat(configuration.defaultStringProperty(), is(equalTo(stringTestData.defaultPropertyValue)));
        assertThat(configuration.definedStringProperty(), is(equalTo(stringTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultStringProperty(), is(equalTo(stringTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalStringProperty(), is(equalTo(Option.<String>none())));
        Option<String> some = Option.some(stringTestData.optionalWithValuePropertyValue);
        Option<String> actual = configuration.optionalWithValueStringProperty();
        assertThat(actual, is(equalTo(some)));

        assertThat(configuration.defaultIntProperty(), is(equalTo(Integer.parseInt(intTestData.defaultPropertyValue))));
        assertThat(configuration.definedIntProperty(), is(equalTo(intTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultIntProperty(), is(equalTo(intTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalIntProperty(), is(equalTo(Option.<Integer>none())));
        assertThat(configuration.optionalWithValueIntProperty(), is(equalTo(Option.some(intTestData.optionalWithValuePropertyValue))));

        assertThat(configuration.defaultLongProperty(), is(equalTo(Long.parseLong(longTestData.defaultPropertyValue))));
        assertThat(configuration.definedLongProperty(), is(equalTo(longTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultLongProperty(), is(equalTo(longTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalLongProperty(), is(equalTo(Option.<Long>none())));
        assertThat(configuration.optionalWithValueLongProperty(), is(equalTo(Option.some(longTestData.optionalWithValuePropertyValue))));

        assertThat(configuration.defaultBoolProperty(), is(equalTo(Boolean.parseBoolean(boolTestData.defaultPropertyValue))));
        assertThat(configuration.definedBoolProperty(), is(equalTo(boolTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultBoolProperty(), is(equalTo(boolTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalBoolProperty(), is(equalTo(Option.<Boolean>none())));
        assertThat(configuration.optionalWithValueBoolProperty(), is(equalTo(Option.some(boolTestData.optionalWithValuePropertyValue))));

        assertThat(configuration.defaultDoubleProperty(), is(equalTo(Double.parseDouble(doubleTestData.defaultPropertyValue))));
        assertThat(configuration.definedDoubleProperty(), is(equalTo(doubleTestData.definedPropertyValue)));
        assertThat(configuration.definedWithDefaultDoubleProperty(), is(equalTo(doubleTestData.definedWithDefaultPropertyValue)));
        assertThat(configuration.optionalDoubleProperty(), is(equalTo(Option.<Double>none())));
        assertThat(configuration.optionalWithValueDoubleProperty(), is(equalTo(Option.some(doubleTestData.optionalWithValuePropertyValue))));
    }

    static class TestConfigNoAnnotation {
    }

    @Properties(source = "source")
    interface TestConfigNoProperties {

        @Property(name = "stringProperty")
        String stringProperty();

        @Property(name = "intProperty")
        @DefaultValue("1")
        int integerProperty();
    }

    @Properties(source = "source")
    interface TestConfigUnsupportedType {

        @Property(name = "stringProperty")
        Double stringProperty();
    }

    @Properties(source = "source")
    interface TestConfigValid {

        @Property(name = DEFINED_STRING_PROPERTY)
        String definedStringProperty();

        @Property(name = DEFINED_WITH_DEFAULT_STRING_PROPERTY)
        @DefaultValue("definedWithDefaultStringProperty default value")
        String definedWithDefaultStringProperty();

        @Property(name = DEFAULT_STRING_PROPERTY)
        @DefaultValue(DEFAULT_STRING_PROPERTY_VALUE)
        String defaultStringProperty();

        @Property(name = OPTIONAL_STRING_PROPERTY, optional = true)
        Option<String> optionalStringProperty();

        @Property(name = OPTIONAL_WITH_VALUE_STRING_PROPERTY, optional = true)
        Option<String> optionalWithValueStringProperty();

        @Property(name = DEFAULT_INT_PROPERTY)
        @DefaultValue(DEFAULT_INT_PROPERTY_VALUE)
        int defaultIntProperty();

        @Property(name = DEFINED_INT_PROPERTY)
        int definedIntProperty();

        @Property(name = DEFINED_WITH_DEFAULT_INT_PROPERTY)
        @DefaultValue("131")
        int definedWithDefaultIntProperty();

        @Property(name = OPTIONAL_INT_PROPERTY, optional = true)
        Option<Integer> optionalIntProperty();

        @Property(name = OPTIONAL_WITH_VALUE_INT_PROPERTY, optional = true)
        Option<Integer> optionalWithValueIntProperty();

        @Property(name = DEFAULT_LONG_PROPERTY)
        @DefaultValue(DEFAULT_LONG_PROPERTY_VALUE)
        long defaultLongProperty();

        @Property(name = DEFINED_LONG_PROPERTY)
        long definedLongProperty();

        @Property(name = DEFINED_WITH_DEFAULT_LONG_PROPERTY)
        @DefaultValue("431")
        long definedWithDefaultLongProperty();

        @Property(name = OPTIONAL_LONG_PROPERTY, optional = true)
        Option<Long> optionalLongProperty();

        @Property(name = OPTIONAL_WITH_VALUE_LONG_PROPERTY, optional = true)
        Option<Long> optionalWithValueLongProperty();

        @Property(name = DEFAULT_BOOL_PROPERTY)
        @DefaultValue(DEFAULT_BOOL_PROPERTY_VALUE)
        boolean defaultBoolProperty();

        @Property(name = DEFINED_BOOL_PROPERTY)
        boolean definedBoolProperty();

        @Property(name = DEFINED_WITH_DEFAULT_BOOL_PROPERTY)
        @DefaultValue("true")
        boolean definedWithDefaultBoolProperty();

        @Property(name = OPTIONAL_BOOL_PROPERTY, optional = true)
        Option<Boolean> optionalBoolProperty();

        @Property(name = OPTIONAL_WITH_VALUE_BOOL_PROPERTY, optional = true)
        Option<Boolean> optionalWithValueBoolProperty();

        @Property(name = DEFAULT_DOUBLE_PROPERTY)
        @DefaultValue(DEFAULT_DOUBLE_PROPERTY_VALUE)
        double defaultDoubleProperty();

        @Property(name = DEFINED_DOUBLE_PROPERTY)
        double definedDoubleProperty();

        @Property(name = DEFINED_WITH_DEFAULT_DOUBLE_PROPERTY)
        @DefaultValue("1.2")
        double definedWithDefaultDoubleProperty();

        @Property(name = OPTIONAL_DOUBLE_PROPERTY, optional = true)
        Option<Double> optionalDoubleProperty();

        @Property(name = OPTIONAL_WITH_VALUE_DOUBLE_PROPERTY, optional = true)
        Option<Double> optionalWithValueDoubleProperty();
    }
}
