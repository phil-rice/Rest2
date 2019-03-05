package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;

import java.util.List;
import java.util.Set;

@ToString
@EqualsAndHashCode

public class SimpleListLensDefn<T> implements LensDefn<ISimpleList<T>> {
    static List<String> primitives = List.of("string", "integer", "double", "boolean");

    public final String name;
    public final String primitiveClassName;
    public SimpleListLensDefn(String name, String primitiveClassName) {
        this.name = name;
        this.primitiveClassName = primitiveClassName;
        if (!primitives.contains(primitiveClassName))
            throw new IllegalArgumentException("Cannot make a " + getClass().getSimpleName() + " for " + primitiveClassName + " legal values are " + primitives);
    }
    @Override public String name() { return name; }
    @Override public String asString() {
        return name + "/**" + primitiveClassName;
    }
    @Override public <J> Lens<J, ISimpleList<T>> asLens(JsonParserAndWriter<J> json) { return json.<T>lensToSimpleList(name, primitiveClassName); }
}
