package org.polyglotted.webapp.resources.impl;

import com.typesafe.config.Config;
import lombok.RequiredArgsConstructor;
import org.polyglotted.webapp.resources.Resources;
import org.polyglotted.webapp.resources.Values;
import org.polyglotted.webapp.resources.ValuesProvider;

@RequiredArgsConstructor
public class ResourcesValuesProvider implements ValuesProvider {

    private final Resources resources;

    @Override
    public Values values(String name) {
        return new TypesafeConfigValues(resources.config(name));
    }

    @RequiredArgsConstructor
    private static class TypesafeConfigValues implements Values {
        private final Config config;

        @Override
        public boolean hasValue(String name) {
            return config.hasPath(name);
        }

        @Override
        public String stringValue(String name) {
            return config.getString(name);
        }

        @Override
        public int intValue(String name) {
            return config.getInt(name);
        }

        @Override
        public boolean booleanValue(String name) {
            return config.getBoolean(name);
        }

        @Override
        public long longValue(String name) {
            return config.getLong(name);
        }

        @Override
        public double doubleValue(String name) {
            return config.getDouble(name);
        }
    }
}
