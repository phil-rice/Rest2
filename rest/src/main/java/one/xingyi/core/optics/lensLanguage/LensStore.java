package one.xingyi.core.optics.lensLanguage;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.MapUtils;
import one.xingyi.core.utils.Sets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
public class LensStore<J> {

    private final Map<String, Lens<Object, Object>> map;
    private JsonParserAndWriter<J> json;


    public LensStore(JsonParserAndWriter<J> json, LensDefnStore store) {
        this.json = json;
        this.map = Lists.<Map<String, Lens<Object, Object>>, LensLine>aggLeft(new HashMap<String, Lens<Object, Object>>(), store.defns,
                (map, line) -> map.put(line.lensName, Lists.foldLeft(Lens.<Object>identity(),
                        line.defns, (lens, defn) -> lens.andThen(defn.<Object>asLens(json)))));
    }
    Supplier<RuntimeException> illegallens(String lensName) {
        return () -> {
            throw new RuntimeException("cannot find lens: " + lensName + "\nLegal values are" + Sets.sortedString(map.keySet(), ","));
        };
    }
    public Lens<J, String> stringLens(String lensName) { return (Lens) Optional.ofNullable(map.get(lensName)).orElseThrow(illegallens(lensName)); }
    public Lens<J, Integer> integerLens(String lensName) { return (Lens) Optional.ofNullable(map.get(lensName)).orElseThrow(illegallens(lensName));}
    public Lens<J, Double> doubleLens(String lensName) { return (Lens) Optional.ofNullable(map.get(lensName)).orElseThrow(illegallens(lensName)); }
    public Lens<J, Boolean> booleanLens(String lensName) { return (Lens) Optional.ofNullable(map.get(lensName)).orElseThrow(illegallens(lensName)); }
    public Lens<J, IResourceList<J>> listLens(String lensName) { return (Lens) Optional.ofNullable(map.get(lensName)).orElseThrow(illegallens(lensName)); }
    public <T> Lens<J, ISimpleList<T>> simpleListLens(String lensName) { return (Lens) Optional.ofNullable(map.get(lensName)).orElseThrow(illegallens(lensName)); }
}
