package io.polyglotted.app.core;

import com.google.common.collect.ImmutableMap;
import fj.data.Option;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Overrides {
    private final Map<String, Object> map;

    public Option<String> string(String key) {
        return Option.fromNull((String)map.get(key));
    }

    @SuppressWarnings("unchecked")
    public <T> Option<T> object(String key) {
        return Option.fromNull((T)map.get(key));
    }

    public Option<Integer> integer(String key) {
        return Option.fromNull((Integer)map.get(key));
    }

    public Option<Long> longValue(String key) {
        return Option.fromNull((Long)map.get(key));
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
