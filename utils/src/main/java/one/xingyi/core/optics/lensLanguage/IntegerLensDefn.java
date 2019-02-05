package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class IntegerLensDefn implements LensDefn {
    public final String name;
    @Override public String name() { return name; }
    @Override public String asString() { return name + "/integer"; }

}
