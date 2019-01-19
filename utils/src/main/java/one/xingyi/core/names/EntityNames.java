package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EntityNames {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName serverEntity;
    public final PackageAndClassName serverCompanion;
    public final PackageAndClassName clientEntity;
}
