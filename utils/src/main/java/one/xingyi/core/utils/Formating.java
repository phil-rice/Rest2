package one.xingyi.core.utils;
import one.xingyi.core.codeDom.PackageAndClassName;

import java.util.Arrays;
import java.util.List;
public interface Formating {
    String indent = "    ";
    static List<String> indent(List<String> list) {
        return Lists.map(list, s -> indent + s);
    }
    static List<String> javaFile(PackageAndClassName packageAndClassName, String classPostFix, Class<?>... imports) {
        return Lists.append(List.of("package " + packageAndClassName.packageName + ";"),
                Lists.map(Arrays.asList(imports), i -> "import " + i.getName() + ";"),
                List.of("public class " + packageAndClassName.className + " " + classPostFix + "{")
        );
    }
}
