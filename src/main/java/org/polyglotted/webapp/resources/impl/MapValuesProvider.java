package org.polyglotted.webapp.resources.impl;

import com.google.common.collect.ImmutableMap;
import org.polyglotted.webapp.resources.Values;
import org.polyglotted.webapp.resources.ValuesProvider;

import java.util.HashMap;
import java.util.Map;

public class MapValuesProvider implements ValuesProvider {

    private final Map<String, Values> values;

    @Override
    public Values values(String name) {
        return values.get(name);
    }

    public MapValuesProvider(Map<String, Map<String, Object>> properties) {
        this.values = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : properties.entrySet()) {
            values.put(entry.getKey(), new MapValues(entry.getValue()));
        }
    }

    public static MapValuesProvider forSource(String source, Map<String, Object> properties) {
        return new MapValuesProvider(ImmutableMap.of(source, properties));
    }

    private static class MapValues implements Values {

        private final Map<String, Object> properties;

        private MapValues(Map<String, Object> properties) {
            this.properties = ImmutableMap.copyOf(properties);
        }

        @Override
        public boolean hasValue(String name) {
            return properties.containsKey(name);
        }

        @Override
        public String stringValue(String name) {
            return validateReturn(name, String.class);
        }

        @Override
        public int intValue(String name) {
            return validateReturn(name, Integer.class);
        }

        @Override
        public boolean booleanValue(String name) {
            return validateReturn(name, Boolean.class);
        }

        @Override
        public long longValue(String name) {
            return validateReturn(name, Long.class);
        }

        @Override
        public double doubleValue(String name) {
            return validateReturn(name, Double.class);
        }

        private <T> T validateReturn(String propertyName, Class<T> clazz) {
            Object o = properties.get(propertyName);
            if (o != null && !o.getClass().isAssignableFrom(clazz)) {
                throw new ConfigException("Invalid type of property " + propertyName +
                        ". Expected: " + clazz.getName() + " actual: " + o.getClass());
            }
            return clazz.cast(o);
        }
    }
}
