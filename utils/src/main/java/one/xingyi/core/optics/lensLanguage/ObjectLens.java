package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.optics.Lens;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ObjectLens implements LensDefn {
    public final String name;
    public final String childClassName;
    @Override public String name() { return name; }
}
