package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;

import java.util.List;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ListLensDefn <T>implements LensDefn<IResourceList<T>> {
    public final String name;
    public final String childClassName;
    @Override public String name() { return name; }
    @Override public String asString() {
        return name + "/*" + childClassName;
    }
    @Override public <J> Lens<J, IResourceList<T>> asLens(JsonParserAndWriter<J> json) { return json.<T>lensToResourceList(name); }
}
