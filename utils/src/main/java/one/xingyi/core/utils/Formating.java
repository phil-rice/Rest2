package one.xingyi.core.utils;
import java.util.List;
public interface Formating {
    String indent = "    ";
    static List<String> indent(List<String> list) {
        return Lists.map(list, s -> indent + s);
    }
}
