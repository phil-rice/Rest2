package one.xingyi.core.javascript;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.sdk.IXingYiClientEntity;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.utils.Digestor;
import one.xingyi.core.utils.Lists;

import java.util.Arrays;
import java.util.List;


public interface JavascriptStore {
    List<JavascriptDetails> find(List<String> lensNames);

    static JavascriptStore constant(String javascript) { return new ConstantJavascriptStore(Digestor.sha256, javascript);}
    static JavascriptStore fromEntities(String rootjavascript, List<IXingYiServerCompanion<?, ?>> companions) {
        return constant(Lists.foldLeft(rootjavascript, companions, (acc, c) -> acc + "\n" + c.javascript()));
    }
}
@ToString
@EqualsAndHashCode
class ConstantJavascriptStore implements JavascriptStore {
    private final List<JavascriptDetails> javascriptDetails;
    public ConstantJavascriptStore(Digestor digestor, String javascript) {
        this.javascriptDetails = List.of(new JavascriptDetails("constant", digestor.apply(javascript), javascript));
    }

    @Override public List<JavascriptDetails> find(List<String> lensNames) {
        return javascriptDetails;
    }
}