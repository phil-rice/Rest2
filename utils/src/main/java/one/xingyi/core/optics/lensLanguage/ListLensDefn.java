package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ListLensDefn implements LensDefn {
    public final String name;
    public final String childClassName;
    @Override public String name() { return name; }
    @Override public String asString() {
        return name + "/*" + childClassName;
    }
}
