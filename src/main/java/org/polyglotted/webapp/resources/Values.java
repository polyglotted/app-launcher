package org.polyglotted.webapp.resources;

public interface Values {

    public boolean hasValue(String name);
    public String stringValue(String name);
    public int intValue(String name);
    public boolean booleanValue(String name);
    public long longValue(String name);
    public double doubleValue(String name);
}

