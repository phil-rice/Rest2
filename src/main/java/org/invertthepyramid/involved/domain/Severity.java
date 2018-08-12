package org.invertthepyramid.involved.domain;

public class Severity {
    String name;

    public Severity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Severity(" + name + ")";
    }

    public static final Severity CRITICAL = new Severity("critical");
    public static final Severity FATAL = new Severity("fatal");
}
