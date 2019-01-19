package one.xingyi.core.sdk;
import one.xingyi.core.javascript.JavascriptStore;
public interface IXingYiServerCompanion<Defn extends IXingYiEntityDefn, T extends IXingYiEntity> {

    String bookmark();
    JavascriptStore javascriptStore();
}
