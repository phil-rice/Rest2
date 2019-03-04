package one.xingyi.core.optics.lensLanguage;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;

@EqualsAndHashCode @ToString
public class FirstItemInListDefn<T> implements LensDefn<T> {
    @Override public String name() { return "{firstItem}"; }
    @Override public String asString() { return name(); }

    //OK the types are a mess here. J isn't a Json thing it's a IResourceList
    @Override public <J> Lens<J, T> asLens(JsonParserAndWriter<J> json) {
        Getter<J, T> getter = j -> ((IResourceList<T>) j).get(0);
        Setter<J, T> setter = (j, newT) -> (J) ((IResourceList<T>) j).withItem(0, newT);
        return Lens.create(getter, setter);
    }
}
