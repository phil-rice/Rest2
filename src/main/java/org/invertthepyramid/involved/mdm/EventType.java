package org.invertthepyramid.involved.mdm;

public class EventType {
    public EventType(String name) {
        this.name = name;
    }

    String name;
    public static final EventType TECHNICAL =  new EventType("technical");
}
