package one.xingyi.core.javascript;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.DigestAndString;
@ToString
@EqualsAndHashCode
public class JavascriptDetails {
    final String name;
    final String digest;
    final String javascriptFragment;
    public JavascriptDetails(String name, DigestAndString digestAndString) {
        this.name = name;
        this.digest = digestAndString.digest;
        this.javascriptFragment = digestAndString.string;
    }
//    public static JavascriptDetails apply(String name, String defn) {
//        return new JavascriptDetails(name, defn.hashCode() + "", defn);
//    }
}
