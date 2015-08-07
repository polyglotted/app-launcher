package io.polyglotted.app.resources;

public interface Values {

    boolean hasValue(String name);
    String stringValue(String name);
    int intValue(String name);
    boolean booleanValue(String name);
    long longValue(String name);
    double doubleValue(String name);
}

