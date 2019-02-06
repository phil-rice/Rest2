package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DoubleLensDefn implements LensDefn<Double> {
    public final String name;
    @Override public String name() { return name; }
    @Override public String asString() { return name + "/double"; }
    @Override public <J> Lens<J, Double> asLens(JsonParserAndWriter<J> json) { return json.lensToDouble(name); }
}
