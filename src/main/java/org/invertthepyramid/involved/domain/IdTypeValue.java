package org.invertthepyramid.involved.domain;

public class IdTypeValue {
    private String id;
    private String type;
    private String value;
    private String name;

    public IdTypeValue(String id, String type, String value, String name) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
