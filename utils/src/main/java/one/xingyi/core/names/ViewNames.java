package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ViewNames {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName clientView;
    public final PackageAndClassName clientCompanion;
}
