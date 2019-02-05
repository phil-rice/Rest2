package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
public class LensStore<J> {
    public JsonParserAndWriter<J> parser;
    public final List<LensLine> defns;

    public LensStore(List<LensLine> defns) {
        this.defns = defns;
    }

    public <E> Lens<J, String> stringLens(String person_name) {
        return null;
    }
}
