package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CompositeViewDom {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName clientResource;
    public final PackageAndClassName clientInterface;
    public final PackageAndClassName clientImpl;

}
