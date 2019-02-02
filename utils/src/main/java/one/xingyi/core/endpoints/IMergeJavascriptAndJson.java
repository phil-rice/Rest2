package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.utils.Digestor;
public interface IMergeJavascriptAndJson {

    String merge(String rootUrl, String javascript, String json);

    static IMergeJavascriptAndJson simple = new SimpleMergeJavascriptAndJson();
    static IMergeJavascriptAndJson byLinks(String prefix) {return new ByLinksJavascriptAndJson(prefix);}
    ;
}
class SimpleMergeJavascriptAndJson implements IMergeJavascriptAndJson {
    @Override public String merge(String rootUrl, String javascript, String json) {
        return javascript + IXingYiResponseSplitter.marker + json;
    }
}
@RequiredArgsConstructor
class ByLinksJavascriptAndJson implements IMergeJavascriptAndJson {
    final String rootUrl;
    @Override public String merge(String rootUrl, String javascript, String json) {
        return this.rootUrl + rootUrl + "/" + Digestor.digestor().apply(javascript).digest + IXingYiResponseSplitter.marker + json;
    }
}
