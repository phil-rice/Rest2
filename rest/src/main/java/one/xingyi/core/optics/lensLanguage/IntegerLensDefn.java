package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class IntegerLensDefn implements LensDefn<Integer> {
    public final String name;
    @Override public String name() { return name; }
    @Override public String asString() { return name + "/integer"; }
    @Override public <J> Lens<J, Integer> asLens(JsonParserAndWriter<J> json) { return json.lensToInteger(name); }

}
