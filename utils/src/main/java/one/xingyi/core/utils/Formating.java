package one.xingyi.core.utils;
import one.xingyi.core.codeDom.PackageAndClassName;

import java.util.Arrays;
import java.util.List;
public interface Formating {
    String indent = "    ";
    static List<String> indent(List<String> list) {
        return Lists.map(list, s -> indent + s);
    }
    static List<String> javaFile(Class<?> madeBy, PackageAndClassName entityDefn, String classOrInterface, PackageAndClassName packageAndClassName, String classPostFix, List<String> manualImports, Class<?>... imports) {
        return Lists.append(List.of("package " + packageAndClassName.packageName + ";", "//Made by " + madeBy.getName(), "//Defn " + entityDefn.asString()),
                Lists.map(Arrays.asList(imports), i -> "import " + i.getName() + ";"),
                Lists.map(manualImports, i -> "import " + i + ";"),
                List.of("public " + classOrInterface + " " + packageAndClassName.className + " " + classPostFix + "{")
        );
    }
}
