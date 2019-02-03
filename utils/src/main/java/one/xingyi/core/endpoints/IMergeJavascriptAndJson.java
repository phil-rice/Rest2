package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.utils.Digestor;
public interface IMergeJavascriptAndJson {

    String merge(String rootUrl, String javascript, String json);

    static IMergeJavascriptAndJson simple = new SimpleMergeJavascriptAndJson();
    static IMergeJavascriptAndJson byLinks = new ByLinksJavascriptAndJson();
    ;
}
class SimpleMergeJavascriptAndJson implements IMergeJavascriptAndJson {
    @Override public String merge(String rootUrl, String javascript, String json) {
        return javascript + IXingYiResponseSplitter.marker + json;
    }
}
@RequiredArgsConstructor
class ByLinksJavascriptAndJson implements IMergeJavascriptAndJson {
    @Override public String merge(String rootUrl, String javascript, String json) {

        String digest = Digestor.digestor().apply(javascript).digest;
        String codeUrl = rootUrl.contains("{id}") ? rootUrl.replace("{id}", "code/" + digest) : rootUrl + "/code/" + digest; //TODO THis is an utter bodge. Need to sort this out
        return codeUrl + IXingYiResponseSplitter.marker + json;
    }
}
