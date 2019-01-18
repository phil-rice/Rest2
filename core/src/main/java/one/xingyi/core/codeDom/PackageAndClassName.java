package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.Strings;

import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PackageAndClassName {
    public static PackageAndClassName stringPn = new PackageAndClassName(String.class.getName());
    public static PackageAndClassName intPn = new PackageAndClassName(Integer.class.getName());
    public static PackageAndClassName doublePn = new PackageAndClassName(Double.class.getName());
    public static PackageAndClassName booleanPn = new PackageAndClassName(Boolean.class.getName());


    public final String packageName;
    public final String className;

    public PackageAndClassName(String name) {
        packageName = Strings.allButLastSegment(".", name);
        className = name.substring(packageName.length() + 1);
    }
    public PackageAndClassName mapName(Function<String, String> fn) {
        return new PackageAndClassName(packageName, fn.apply(className));
    }
    public String asString() {
        return packageName + "." + className;
    }
    public PackageAndClassName withName(String name) {
        return new PackageAndClassName(packageName, name);
    }
}
