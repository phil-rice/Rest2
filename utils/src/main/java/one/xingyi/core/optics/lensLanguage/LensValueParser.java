package one.xingyi.core.optics.lensLanguage;

import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


public interface LensValueParser extends Function<String, List<LensDefn>> {
    static LensValueParser simple() {
        return new SimpleLensParser();
    }
}

class SimpleLensParser implements LensValueParser {
    Map<String, Function<String, StringLensDefn>> primitives = Map.of(
            "integer", name -> new StringLensDefn(name, "integer"),
            "string", name -> new StringLensDefn(name, "string"),
            "double", name -> new StringLensDefn(name, "double"));

    @Override
    public List<LensDefn> apply(String s) {
        return Lists.map(Strings.split(s, ","), item -> {
            List<String> parts = Strings.split(item, "/");
            if (parts.size() != 2)
                throw new RuntimeException("could not find two parts in item " + item + " which is in " + s);
            String name = parts.get(0);
            String type = parts.get(1);
            Function<String, StringLensDefn> fn = primitives.get(type);
            if (fn == null) return new ObjectLens(name, type);
            return fn.apply(name);
        });
    }
}
