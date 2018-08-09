package org.invertthepyramid.involved.misc;

public class Severity {
    String name;

    public Severity(String name) {
        this.name = name;
    }

    public static final Severity CRITICAL = new Severity("critical");
    public static final Severity FATAL = new Severity("fatal");
}
