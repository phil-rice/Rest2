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
    Map<String, Function<String, LensDefn>> primitives = Map.of(
            "integer", name -> new IntegerLensDefn(name),
            "string", name -> new StringLensDefn(name),
            "double", name -> new DoubleLensDefn(name));

    @Override
    public List<LensDefn> apply(String s) {
        return Lists.map(Strings.split(s, ","), item -> {
            if (item.equalsIgnoreCase("<firstItem>")) return new FirstItemInListDefn();
            List<String> parts = Strings.split(item, "/");
            if (parts.size() != 2)
                throw new RuntimeException("could not find two parts in item " + item + " which is in " + s);
            String name = parts.get(0);
            String type = parts.get(1);
            Function<String, LensDefn> fn = primitives.get(type);
            if (fn == null && type.startsWith("*")) return new ListLensDefn(name, type.substring(1));
            if (fn == null) return new ViewLensDefn(name, type);
            return fn.apply(name);
        });
    }
}
