package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Sets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EqualsAndHashCode
@ToString
public class LensDefnStore {
    public final List<LensLine> defns;
    public LensDefnStore(List<LensLine> defns) {
        this.defns = defns;
//        System.out.println("LensStore:\n" + Lists.mapJoin(defns, "\n", Objects::toString));

    }
    public <J> LensStore<J> makeStore(JsonParserAndWriter<J> parser) {
        return new LensStore<>(parser, this);
    }
}

