package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ViewNames {
    final PackageAndClassName originalDefn;
    final PackageAndClassName clientView;
    final PackageAndClassName clientCompanion;
}
