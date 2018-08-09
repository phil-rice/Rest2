package org.invertthepyramid.involved.misc;

public class EventType {
    public EventType(String name) {
        this.name = name;
    }

    String name;
    public static final EventType TECHNICAL =  new EventType("technical");
}
