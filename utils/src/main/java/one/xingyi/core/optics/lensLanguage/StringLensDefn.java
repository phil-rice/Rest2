package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.optics.Lens;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class StringLensDefn implements LensDefn {
    public final String name;
    @Override public String name() { return name; }
    @Override public String asString() { return name + "/string"; }

}
