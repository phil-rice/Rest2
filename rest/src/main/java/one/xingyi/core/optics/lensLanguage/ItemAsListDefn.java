package one.xingyi.core.optics.lensLanguage;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;

@EqualsAndHashCode @ToString
public class ItemAsListDefn<T> implements LensDefn<T> {
    @Override public String name() { return "<itemAsListDefn>"; }
    @Override public String asString() { return name(); }

    @Override public <J> Lens<J, T> asLens(JsonParserAndWriter<J> json) {
        Getter<J, T> getter = j ->
                (T) IResourceList.create(j);
        Setter<J, T> setter = (j, newT) -> (J) ((IResourceList<T>) j).get(0);
        return Lens.create(getter, setter);
    }
}
