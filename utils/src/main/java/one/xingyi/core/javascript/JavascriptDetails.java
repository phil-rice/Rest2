package one.xingyi.core.javascript;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class JavascriptDetails {
    final String name;
    final String digest;
    final String javascriptFragment;
//    public static JavascriptDetails apply(String name, String javascript) {
//        return new JavascriptDetails(name, javascript.hashCode() + "", javascript);
//    }
}
