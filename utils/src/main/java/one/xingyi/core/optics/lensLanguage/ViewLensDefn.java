package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ViewLensDefn implements LensDefn<Object> {
    public final String name;
    public final String childClassName;
    @Override public String name() { return name; }
    @Override public String asString() { return name + "/" + childClassName;}
    @Override public <J> Lens<J, Object> asLens(JsonParserAndWriter<J> json) { return (Lens<J, Object>) json.lensToChild(name); }
}
