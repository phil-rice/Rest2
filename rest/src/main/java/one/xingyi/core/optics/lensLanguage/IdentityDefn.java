package one.xingyi.core.optics.lensLanguage;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;

@EqualsAndHashCode @ToString
public class IdentityDefn<T> implements LensDefn<T> {
    @Override public String name() { return "{identity}"; }
    @Override public String asString() { return name(); }

    @Override public <J> Lens<J, T> asLens(JsonParserAndWriter<J> json) {
        return (Lens<J, T>) Lens.identity();
    }
}
