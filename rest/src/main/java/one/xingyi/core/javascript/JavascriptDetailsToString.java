package one.xingyi.core.javascript;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.function.Function;
public interface JavascriptDetailsToString extends Function<List<JavascriptDetails>, String> {
    public static JavascriptDetailsToString simple = new SimpleJavascriptDetailsToString();
}

class SimpleJavascriptDetailsToString implements JavascriptDetailsToString {

    @Override public String apply(List<JavascriptDetails> javascriptDetails) {
        return Lists.mapJoin(javascriptDetails, "\n", e -> e.javascriptFragment);
    }
}
