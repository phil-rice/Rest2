package one.xingyi.core.optics.lensLanguage;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;
public class FirstItemInListDefn<T> implements LensDefn<T> {
    @Override public String name() {
        return "<firstItem>";
    }
    @Override public String asString() {
        return name();
    }
    @Override public <J> Lens<J, T> asLens(JsonParserAndWriter<J> json) {
        Getter<J, T> getter = j -> (T) json.asList(j).get(0);
        Setter<J, T> setter = (j, newT) -> (J) json.asResourceList(j).withItem(0, (J) newT);
        return Lens.create(getter, setter);
    }
}
