package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class LensDefnStore {
    public final List<LensLine> defns;
}

class LensStore {

    private final Map<String, Lens<Object, Object>> map;

    public LensStore(JsonParserAndWriter json, LensDefnStore store) {
        this.map = Lists.aggLeft(new HashMap<String, Lens<Object, Object>>(), store.defns, (map, line) -> map.put(line.lensName, Lists.foldLeft(Lens.<Object>identity(), line.defns, (lens, defn) -> lens.andThen(defn.<Object>asLens(json)))));
    }
}
