package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ViewAndCompanion {
    public final PackageAndClassName view;
    public final PackageAndClassName companion;
}
