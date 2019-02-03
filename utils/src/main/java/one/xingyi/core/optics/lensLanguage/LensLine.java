package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class LensLine {
    public final String lensName;
    public final List<LensDefn> defns;
    public <J, To> Lens<J, To> asLens(JsonParserAndWriter<J> parser) {
//        return Lens.create(getter, setter);
        return null;
    }
}
