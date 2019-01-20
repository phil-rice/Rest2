package one.xingyi.core.javascript;
import one.xingyi.core.utils.Digestor;

import java.util.List;


public interface JavascriptStore {
    List<JavascriptDetails> find(List<String> lensNames);

    //    static JavascriptStore finder(Digestor digestor, JavascriptFor javascriptFor) {return new SimpleJavascriptStore(digestor, javascriptFor);}
    static String javascript(JavascriptStore store, List<String> lensNames) {
        return "all the javascript in one place for now";
    }

}