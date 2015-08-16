package io.polyglotted.app.core;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Overrides {
    private final Map<String, Object> map;

    public Optional<String> string(String key) {
        return Optional.ofNullable((String) map.get(key));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> object(String key) {
        return Optional.ofNullable((T) map.get(key));
    }

    public Optional<Integer> integer(String key) {
        return Optional.ofNullable((Integer) map.get(key));
    }

    public Optional<Long> longValue(String key) {
        return Optional.ofNullable((Long)map.get(key));
    }

    public static Overrides fromMap(Map<String, Object> map) {
        return new Overrides(map);
    }

    public static Overrides value(String key, Object value) {
        return fromMap(ImmutableMap.of(key, value));
    }

    public static Overrides values(String key1, Object value1, String key2, Object value2) {
        return fromMap(ImmutableMap.of(key1, value1, key2, value2));
    }

    public static Overrides values(String key1, Object value1,
                                   String key2, Object value2,
                                   String key3, Object value3) {
        return fromMap(ImmutableMap.of(key1, value1, key2, value2, key3, value3));
    }

    public static Overrides empty() {
        return new Overrides(ImmutableMap.<String, Object>of());
    }
}
